package org.judeukkim.springbootKDJ.controller;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.BoardDto;
import org.judeukkim.springbootKDJ.dto.ReplyDto;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.service.BoardService;
import org.judeukkim.springbootKDJ.service.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0,size = 5,sort = "id",
                                    direction = Sort.Direction.DESC) Pageable pageable){

            Page<BoardDto> boardList = boardService.boardPagingList(pageable);

            Long total=boardList.getTotalElements();//전체레코드수
            int blockNum=5; //화면에 보여지는 페이지 인덱스 블록의 갯수 < 1, 2, 3, 4, 5... > <- 이거
            int nowPage= boardList.getNumber()+1;  //현재페이지 -> boardList.getNumber()는 0부터 시작해서 +1붙여야함
            int startPage=Math.max(1,boardList.getNumber()-blockNum);  //시작페이지-> 기본이 최소 1페이지
            int endPage=boardList.getTotalPages();  //마지막페이지

            model.addAttribute("total",total);  //전체레코드수
            model.addAttribute("boardList",boardList);  //boardList(게시글리스트)
            model.addAttribute("nowPage",nowPage);      //현재페이지
            model.addAttribute("startPage",startPage);  //시작페이지
            model.addAttribute("endPage",endPage);      //끝페이지

        return "board/list";
    }


    @GetMapping("/board/write")
    public String saveForm(@AuthenticationPrincipal UserDetails userDetails,Model model){
        //시큐리티에 저장된 회원정보 가져가기
        String loginUser = userDetails.getUsername();
        // 로그인 아이디를 따로 저장해 둔다.

        model.addAttribute("userName", loginUser);
        model.addAttribute("boardDto", new BoardDto());

        return "board/saveForm";
    }

//    @PostMapping("board/write")
//    public String write(BoardDto boardDto){
//        boardService.boardWrite(boardDto);
//
//        return "redirect:/board/list";
//    }
    @PostMapping("board/write")
    public String write(BoardDto boardDto) throws IOException{
        boardService.boardWrite(boardDto);

        return "redirect:/board/list";
    }

    @GetMapping("/board/detail/{id}")
    public String boardDetail(@PathVariable Long id, Authentication authentication, Model model) {
        boardService.hitCount(id); //조회수 증가 처리
        BoardDto boardDto = boardService.boardDetailView(id);

        if (boardDto != null) {
            model.addAttribute("detail", boardDto);


            if (authentication != null) {
                String loginUser = authentication.getName();
                model.addAttribute("userName", loginUser);
            }

            System.out.println("newFile=============="+ boardDto.getNewFileName() );
            List<ReplyDto> replyList = replyService.replyDtoListDo(id);
            model.addAttribute("replyList", replyList);
            return "board/detailView";
        } else {
            return null;
        }
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, Model model,
                              @AuthenticationPrincipal UserDetails userDetails ){
        BoardDto boardDto = boardService.boardDetailView(id);
        //시큐리티에 저장된 회원정보 가져가기
        String loginUser = userDetails.getUsername();
        // 로그인 아이디를 따로 저장해 둔다.

        model.addAttribute("userName", loginUser);
        model.addAttribute("update",boardDto);
        return "board/updateView";
    }

    @PostMapping("/board/update")
    public String boardUpdatePost( @ModelAttribute BoardDto boardDto) throws IOException {

        boardService.boardUpdate(boardDto);
        return "redirect:/board/list";
    }
    @GetMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id){
        int rs= boardService.deleteOk(id);
        if (rs==1){
            System.out.println("게시글삭제 실패");
            return null;
        }
        System.out.println("게시글삭제 성공");
        return "redirect:/board/list";
    }
}

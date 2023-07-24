package org.judeukkim.springbootKDJ.controller;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.ReplyDto;
import org.judeukkim.springbootKDJ.service.ReplyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replyWrite")
    public String replyWrite(@ModelAttribute ReplyDto replyDto, Model model){

        // 확인 boardId 확인->    1.댓글저장
        replyService.insertReplyDo(replyDto); // 확인 boardId 확->    1.덧글저장

        // 댓글 리스트 -> 게시글 id(댓글을 단 게시글의 id)의 리스트만 get
        List<ReplyDto> replyDtoList= replyService.replyDtoListDo(replyDto.getBoardId());
        //댓글을 작성하고 -> 게시글이 존재하는지 확인 -> DTO-> Entity -> 저장

        // 상세페이지에   글번호와 댓글리스트를 가지고 같이 보내야된다.
      model.addAttribute("replyList",replyDtoList);
        //게시글 id  -> replyDto.getBoardId()
        return "redirect:/board/detail/"+ replyDto.getBoardId();
    }
//    @PostMapping("/replyUpdate")
//    public String replyUpdate(@ModelAttribute ReplyDto replyDto, Model model){
//
//        Long updateReply=replyService.updateReply(replyDto);
//
//        model.addAttribute("updateReply",updateReply);
//
//        return "redirect:/board/detail/"+ replyDto.getBoardId();
//    }
    @GetMapping("/replyDelete")
    public String replyDelete(@RequestParam Long boardId, @RequestParam Long replyId){

        replyService.replyDelete(replyId,boardId);

        return "redirect:/board/detail/"+boardId;
    }
}

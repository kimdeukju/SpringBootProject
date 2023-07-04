$('#pwSearch').on('click',searchFn);

function searchFn(){
         const data={
             email:$('#email').val(),
             userName:$('#userName').val()
                  };
         $.ajax({
             type: "POST",
             url: "/pwSearch",
             data: JSON.stringify(data), //전송할 데이터를 JSON 형식으로 변환하여 설정합니다. 앞서 생성한 data 객체가 전송됩니다.
             contentType: "application/json; charset=utf-8" //전송할 데이터의 콘텐츠 타입을 설정합니다.
         }).done(function(kdj) {  //메서드: 서버 요청이 성공했을 때 호출되는 콜백 함수입니다. kdj 매개변수로 서버의 응답을 받아옵니다.
           			if (kdj.status == 400) { //서버의 응답 상태가 400이면 아래의 코드를 실행합니다. SmtpDto에 생성
           				if (kdj.data.hasOwnProperty('email')) {  //응답 데이터 객체(kdj.data)에 email 프로퍼티가 있는지 확인합니다. 이를 통해 이메일 관련 오류가 있는지 확인할 수 있습니다.
           					$('#email').text(kdj.data.valid_email); // email 요소의 텍스트를 오류 메시지(valid_email)로 설정합니다.
           					$('#email').focus(); //email 요소에 포커스를 줍니다.
           				} else {
           					$('#email').text(''); //email 요소의 텍스트를 지웁니다.
           				}
           				if (kdj.data.hasOwnProperty('userName')) { //: 응답 데이터 객체(resp.data)에 policeNumber 프로퍼티가 있는지 확인합니다. 이를 통해 경찰 번호 관련 오류가 있는지 확인할 수 있습니다.
           					$('#userName').text(kdj.data.valid_userName);
           					$('#userName').focus();
           				} else {
           					$('#userName').text('');
           				}
           				closeLoadingWithMask();  //로딩 마스크를 닫습니다.
           			} else {
           				alert("임시 비밀번호가 발송되었습니다.");
           				location.href = "/login";
           			}
           		}).fail(function(error) {   // 메서드: 서버 요청이 실패했을 때 호출되는 콜백 함수입니다. error 매개변수로 오류 정보를 받아옵니다.
           			console.log(error); //오류 정보를 콘솔에 로그합니다.
           		});
           	}

function LoadingWithMask() {
    //화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;

    //화면에 출력할 마스크를 설정해줍니다.
    var mask    = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var spinner = "<div id='spinner' style='position: absolute; top: 45%; left: 50%; margin: -16px 0 0 -16px; display: none; color: #4dff93;' class='spinner-border'></div>";
    //화면에 레이어 추가
    $('body')
        .append(mask)
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' : '0.3'
    });
    //마스크 표시
    $('#mask').show();
    //로딩중 이미지 표시
    $('body').append(spinner);
    $('#spinner').show();
}
function closeLoadingWithMask() {
	$('#mask, #spinner').hide();
	$('#mask, #spinner').empty();
}
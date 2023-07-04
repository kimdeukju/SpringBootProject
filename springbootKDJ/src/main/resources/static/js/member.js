  $('#emailChecked').on('click',emailCheckedFn);
  $('#sample4_execDaumPostcode').on('click',sample4_execDaumPostcodeFn);

function emailCheckedFn(){
         const data={
             'email':$('#email').val()
                  };
        $.ajax({
             type:'post', // 요청방식
              url:'/emailChecked', //호출경로
               data: data,
               success:function(res){ //성공
                      if(res==1){
                        alert("email사용 가능")
                      }else{
                        alert("email사용 불가")
                      }
                 },
                error:function(){ // 실패
                console.log('실패');
                    }
            })
        }

    function sample4_execDaumPostcodeFn(){
                      new daum.Postcode({
                        oncomplete: function (data) {
                          var roadAddr = data.roadAddress; // 도로명 주소 변수
                          var extraRoadAddr = ''; // 참고 항목 변수
                          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                            extraRoadAddr += data.bname;
                          }
                          if (data.buildingName !== '' && data.apartment === 'Y') {
                            extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                          }
                          if (extraRoadAddr !== '') {
                            extraRoadAddr = ' (' + extraRoadAddr + ')';
                          }
                          document.getElementById('sample4_postcode').value = data.zonecode;
                          document.getElementById("sample4_roadAddress").value = roadAddr;
                          // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                          if (roadAddr !== '') {
                            document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                          } else {
                            document.getElementById("sample4_extraAddress").value = '';
                          }

                          var guideTextBox = document.getElementById("guide");
                          // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                          if (data.autoRoadAddress) {
                            var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                            guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                            guideTextBox.style.display = 'block';

                          } else if (data.autoJibunAddress) {
                            var expJibunAddr = data.autoJibunAddress;
                            guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                            guideTextBox.style.display = 'block';
                          } else {
                            guideTextBox.innerHTML = '';
                            guideTextBox.style.display = 'none';
                          }
                        }
                      }).open();
                    }
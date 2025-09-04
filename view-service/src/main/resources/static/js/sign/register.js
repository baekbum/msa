// Handles the actual data submission for signin
const handleSignup = async (form) => {
    const formData = new FormData(form);
    const data = {};
    // formData를 순회하며 key-value 쌍을 data 객체에 추가합니다.
    for (let [key, value] of formData.entries()) {
        // 'repeat_password' 키는 제외합니다.
        if (key !== 'repeat_password') {
            data[key] = value;
        }
    }
    const endpoint = 'http://localhost:8000/user-service/add';

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            console.log('회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.');
            alert('회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.');
            window.location.href = '/sign/login'; // login 화면으로 이동
        } else {
            const errorText = await response.text();
            console.error('회원가입 실패:', errorText);
            alert(errorText);
        }
    } catch (error) {
        console.error('네트워크 오류:', error);
        alert('네트워크 오류가 발생했습니다. 다시 시도해 주세요.');
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');

    console.log('회원가입 폼:', registerForm);

    // form의 제출 이벤트를 감지
    registerForm.addEventListener('submit', (event) => {
        // 페이지가 새로고침되는 기본 동작을 막음
        event.preventDefault();

        const password = document.getElementById('password').value;
        const repeatPassword = document.getElementById('repeat_password').value;

        if (password !== repeatPassword) {
            alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
            return; // 비밀번호가 일치하지 않으면 함수 실행 중단
        }
        
        // handleSignup 함수 호출
        handleSignup(registerForm);
    });
});

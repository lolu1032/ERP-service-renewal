$(document).ready(function() {
    // 변수 초기화
    let supplierList = []; // 협력 회사 리스트
    let supplierEmail = '';
    let selectItem = [];
    let checkData = false;
    let isModalOpen = false;
    let itemInfo = [];


    // 모달 열기 함수
    function openModal(modalId) {
        if (!isModalOpen) {
            $(modalId).modal('show');
            isModalOpen = true; // 모달 열림 상태로 변경
        }
    }

    function closeModal(modalId) {
        $(modalId).modal('hide');
        isModalOpen = false; // 모달 닫힘 상태로 변경
    }

    function sendEmail(){
        console.log("보낸 이메일 : " + supplierEmail);
        $.ajax({
            url: '/purchase/orderCreate', // 요청할 URL
            type: 'POST', // 요청 방식 (GET, POST 등)
            data: JSON.stringify({"targetMail": supplierEmail}), // JSON 형식으로 데이터 전송
            contentType: 'application/json', // JSON 형식으로 전송
            dataType: 'json', // 서버에서 반환할 데이터 형식
            success: function(data) {
                console.log(data); // 받은 데이터 처리
                console.log("메일발송 성공");
//                alert("메일발송 성공");
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                console.log("메일발송 실패");
//                alert("메일발송 실패");
            }
        });
    }

    // .check-item에 대한 change 이벤트를 바인딩
    $('.check-item').off('change').on('change', function() {
        // 이벤트 핸들러 코드
        if (this.checked) {
            selectItem = []; // 값 초기화
            $('.check-item').not(this).prop('checked', false); // 다른 체크박스는 체크 해제
            $('tr[id^="list-item-"]').each(function() {
                var $row = $(this);
                var $checkbox = $row.find('.check-item');

                // 체크박스가 체크된 경우에만 데이터 추출
                if ($checkbox.is(':checked')) {
                    var quo_id = $row.find('td').eq(1).text().trim();
                    var item_id = $row.find('td').eq(2).text().trim();
                    var date = $row.find('td').eq(3).text().trim();
                    var price = $row.find('td').eq(4).text().trim();
                    var total_amount = $row.find('td').eq(5).text().trim();
                    var status = $row.find('td').eq(6).text().trim();
                    selectItem.push({ quo_id, item_id, date, price, total_amount, status });
                }
            });

            console.log(selectItem);

            checkData = true;
        } else {
            checkData = false;
        }
    });

    $('#contractBtn').on('click', function() {
        if (checkData) {
            // 모든 행을 선택합니다.
            openModal('#contractModal'); // 모달 열기
            $('#modal-quo_id').text(selectItem[0].quo_id);
            $('#modal-item_id').text(selectItem[0].item_id);
            $('#modal-date').text(selectItem[0].date);
            $('#modal-price').text(selectItem[0].price);
            $('#modal-total_amount').text(selectItem[0].total_amount);
            $('#modal-status').text(selectItem[0].status);
        } else {
            alert("리스트를 선택해주세요.");
        }
    });

    $('#closeBtn').on('click', function() {
        closeModal("#orderModal"); // 모달 닫기
        closeModal("#searchModal");
    });

    // 계약서 등록
    $('#quoCreate').on('click', function() {
        // 날짜 계산
        const today = new Date();

        // 2일에서 6일 사이의 랜덤 숫자를 생성
        const randomDays = Math.floor(Math.random() * (6 - 2 + 1)) + 2;

        // 랜덤 날짜를 계산
        const randomDate = new Date(today);
        randomDate.setDate(randomDate.getDate() + randomDays);

        // 날짜를 "yy-MM-dd" 형식으로 포맷팅하는 함수
        function formatDate(date) {
            const year = String(date.getFullYear()).slice(-2); // 마지막 두 자리 연도
            const month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (01~12)
            const day = String(date.getDate()).padStart(2, '0'); // 일 (01~31)
            return `${year}-${month}-${day}`;
        }
        const d_day = formatDate(randomDate);
        selectItem.push({ d_day });
        console.log(selectItem);
        const contractData = {
            total_amount: selectItem[0].total_amount,
            sup_id: selectItem[1].sup_id,
            d_day: selectItem[2].d_day,
        };
        console.log(contractData);
        // AJAX 요청 (주석 해제 후 사용)
        $.ajax({
            url: '/item/addContract',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(contractData),
            success: function(response) {
                alert("계약이 성공적으로 추가되었습니다.");
                closeModal("#contractModal");
            },
            error: function(xhr, status, error) {
                alert("계약등록이 실패했습니다: " + (xhr.status ? xhr.status + ': ' + xhr.statusText : error));
            }
        });

    });

    // 협력업체 값 지정 및 선택 모달 스크립트
    $("#myBtn_in").click(function() {
        $.ajax({
            url: '/purchase/getAllSupplier',
            type: 'POST',
            dataType: 'json',
            success: function(response) {
                if (response.supList && response.supList.length > 100) { // 예시: 최대 100개 항목
                    alert("협력업체 수가 너무 많습니다.");
                    return;
                }
                openModal('#searchModal'); // 모달 열기
                supplierList = response.supList;
                displayPartners(supplierList);
                console.log(response);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
//        $(modalId).modal({
//            backdrop: false // 백드롭 비활성화
//        });
    });

    // 협력업체 행 생성 함수
    function createSupplierRow(item) {
        return `
            <tr class="supplier-row" data-id="${item.sup_id}" data-code="${item.sup_code}" data-name="${item.name}" data-email="${item.email}">
                <td>${item.sup_id}</td>
                <td>${item.sup_code}</td>
                <td>${item.name}</td>
            </tr>
        `;
    }

    $('#order_qty').on("keyup", function() {
        // 수량과 단가 가져오기
        console.log(itemInfo.price);
        const orderQty = parseFloat($('#order_qty').val()) || 0;
        const price = parseFloat(itemInfo.price) || 0;
        // 총 금액 계산
        const totalAmount = orderQty * price;
        // 총 금액을 총 금액 입력란에 대입
        $('#order_value').val(totalAmount);
    });

    // 협력업체 리스트 표시
    function displayPartners(supList) {
        const tbody = $("#supplierList tbody");
        tbody.empty(); // 기존 데이터 제거

        supList.forEach(item => {
            tbody.append(createSupplierRow(item));
        });
    }

    // 검색 기능
    $("#searchInput").on("keyup", function() {
        const query = $(this).val().toLowerCase();
        const filteredPartners = supplierList.filter(supplier =>
            supplier.name.toLowerCase().includes(query)
        );
        displayPartners(filteredPartners);
    });

    // 협력업체 선택
    $(document).on("click", ".supplier-row", function() {
        const sup_id = $(this).data("id");
        const sup_name = $(this).data("name");
        supplierEmail = $(this).data("email");
        $("#order_sup_id").val(sup_id);
        selectItem.push({ sup_id });
        $("#order_sup_name").val(sup_name);
        closeModal("#searchModal"); // 모달 닫기
        console.log(selectItem);
    });

});

// 테이블 업데이트 함수
function updateTable(items) {
    const tableBody = $('#contractTableBody');
    tableBody.empty(); // 테이블 내용 초기화

    items.forEach(item => {
        const res = createTableRow(item);
        tableBody.append(res); // 테이블에 행 추가
    });
}

// 테이블 행 생성 함수
function createTableRow(item) {
    return `
        <tr>
            <td>${item.quo_id}</td>
            <td>${item.item_id}</td>
            <td>${item.date}</td>
            <td>${item.price}</td>
            <td>${item.total_amount}</td>
            <td hidden >${item.status}</td>
        </tr>
    `;
}
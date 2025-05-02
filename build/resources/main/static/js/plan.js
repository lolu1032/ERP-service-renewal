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
            url: '/purchase/sendEmail', // 요청할 URL
            type: 'POST', // 요청 방식 (GET, POST 등)
            data: JSON.stringify({"targetMail": supplierEmail}), // JSON 형식으로 데이터 전송
            contentType: 'application/json', // JSON 형식으로 전송
            dataType: 'json', // 서버에서 반환할 데이터 형식
            success: function(data) {
                console.log(data.msg); // 받은 데이터 처리
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                console.log(data.msg); // 받은 데이터 처리
            }
        });
    }

    // .check-item에 대한 change 이벤트를 바인딩
    $('.check-item').off('change').on('change', function() {
        // 이벤트 핸들러 코드
        if (this.checked) {
            selectItem = []; // 값 초기화
            $('.check-item').not(this).prop('checked', false); // 다른 체크박스는 체크 해제

            const row = $(this).closest('tr');
            const plan_id = row.find('[data-plan_id]').text();
            const item_name = row.find('[data-item_name]').text();
            const qty = row.find('[data-qty]').text();
            const date = row.find('[data-date]').text();
            const leadtime = row.find('[data-leadtime]').text();
            const status = row.find('[data-status]').text();
            selectItem.push({ plan_id, item_name, qty, date, leadtime, status });
            console.log(selectItem);


            // 선택한 품목 찾기
            $.ajax({
                url: '/purchase/getItem',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ item_name: item_name }),
                dataType: 'json',
                success: function(response) {
                    // 아이템 itemInfo에 담기
                    console.log(response);
                    itemInfo = response.itemDTO;
                },
                error: function(xhr, status, error) {
                    console.error('Error:', error);
                }
            });

            checkData = true;
        } else {
            checkData = false;
        }
    });

    $('#orderCreate').on('click', function() {
        if (checkData) {
            openModal('#orderModal'); // 모달 열기
            updateTable(selectItem);
            // selectItem 배열에서 데이터 대입
            $('#order_item_name').val(selectItem[0].item_name); // order_item_name에 데이터 대입
            $('#order_leadtime').val(selectItem[0].leadtime); // order_leadtime에 데이터 대입
        } else {
            alert("리스트를 선택해주세요.");
        }
    });

    $('#closeBtn').on('click', function() {
        closeModal("#orderModal"); // 모달 닫기
        closeModal("#searchModal");
    });

    // 발주서 발행
    $('#createOrder').on('click', function() {
        // 날짜 계산
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(now.getDate()).padStart(2, '0');

        const formattedDate = `${year}${month}${day}`; // 현재 시스템 날짜



        const order_code = "J"+formattedDate+$("#order_sup_id").val()+selectItem[0].plan_id;
        console.log("함보자"+order_code);
        const orderData = {
            sup_id: $("#order_sup_id").val(),
            plan_id: selectItem[0].plan_id,
            sup_name: $("#order_sup_name").val(),
            order_code: order_code,
            item_name: selectItem[0].item_name,
            quantity: $("#order_qty").val(),
            value: $("#order_value").val(),
            status: "발주완료",
            lead_time: selectItem[0].leadtime,
        };

        console.log(orderData);

        // AJAX 요청 (주석 해제 후 사용)
        $.ajax({
            url: '/purchase/orderCreate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(orderData),
            success: function(response) {
                alert("주문이 성공적으로 추가되었습니다.");
                $("#orderModalTableBody").find("input").val(''); // 입력 필드 초기화
                closeModal("#orderModal");
                sendEmail();
                window.location.href = "/purchase/plan";
            },
            error: function(xhr, status, error) {
                alert("주문 추가에 실패했습니다: " + (xhr.status ? xhr.status + ': ' + xhr.statusText : error));
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
                openModal('#searchModal'); // 모달 열기
                supplierList = response.supList;
                displayPartners(supplierList);
                console.log(response);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
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
        const id = $(this).data("id");
        const name = $(this).data("name");
        supplierEmail = $(this).data("email");
        $("#order_sup_id").val(id);
        $("#order_sup_name").val(name);
        closeModal("#searchModal"); // 모달 닫기
    });

});

// 테이블 업데이트 함수
function updateTable(items) {
    const tableBody = $('#planModalTableBody');
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
            <td>${item.plan_id}</td>
            <td>${item.item_name}</td>
            <td>${item.qty}</td>
            <td>${item.date}</td>
            <td>${item.leadtime}</td>
            <td>${item.status}</td>
        </tr>
    `;
}

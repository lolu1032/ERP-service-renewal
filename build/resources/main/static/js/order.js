$(document).ready(function() {

    // 변수 초기화
    let selectItem = [];
    let checkData = false;
    let isModalOpen = false;
    let selectedStatus = "";
    let orderId = 0;
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

    $('.check-item').off('change').on('change', function() {
        // 이벤트 핸들러 코드
        if (this.checked) {
            selectItem = [];

            $('.check-item').not(this).prop('checked', false);

            const row = $(this).closest('tr');
            console.log(row.find('[data-orderId]').text());

            orderId = row.find('[data-orderId]').text();

            const orderData = {
                orderId: orderId,
                orderCode: row.find('[data-orderCode]').text(),
                itemName: row.find('[data-itemName]').text(),
                qty: row.find('[data-qty]').text(),
                leadtime: row.find('[data-leadtime]').text(),
                supName: row.find('[data-supName]').text(),
                value: row.find('[data-value]').text(),
                status: row.find('[data-status]').text(),
                notice: '이상없음',
            };

            selectItem.push(orderData);
            console.log(selectItem);

            checkData = true;
        } else {
            checkData = false;
        }
    });

    $('#inspecOpen').on('click', function() {
        if (checkData) {
            openModal('#inspecModal');
            updateTable(selectItem);
        } else {
            alert("검수 항목을 선택해주세요.");
        }
    });

    $('#inspecSave').on('click', function() {

        const inspecData = {
            orderId: orderId,
            orderCode: $('#orderCode').val(), // 괄호 추가
            itemName: $('#itemName').val(), // 괄호 추가
            qty: $('#qty').val(), // 괄호 추가
            leadtime: $('#leadtime').val(), // 괄호 추가
            status: selectedStatus,
        };

//        console.log("보낼 코드 : " + inspecData[0]);

        $.ajax({
            url: '/purchase/inspectionUpdate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(inspecData),
            dataType: 'json',
            success: function(response) {
                // 아이템 itemInfo에 담기
                console.log(response);
                alert("검수 변경사항 저장");
                window.location.href = "/purchase/order";

            },
            error: function(xhr, status, error) {

                console.error('Error:', error);
            }
        });
    });



//    $('#closeBtn').on('click', function() {
//        closeModal("#orderModal"); // 모달 닫기
//        closeModal("#searchModal");
//    });

    $(document).on('change', '.status-select', function() {
        selectedStatus = $(this).val();
        console.log(selectedStatus);
        const orderCode = $(this).data('order-code');
        console.log(orderCode);
        console.log(`주문 코드: ${orderCode}, 선택된 상태: ${selectedStatus}`);
        // 여기서 선택된 값을 처리하는 로직을 추가할 수 있습니다.
    });

});

// 테이블 업데이트 함수
function updateTable(items) {
    const tableBody = $('#inspectionTableBody');
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
            <td hidden id="orderId">${item.orderId}</td>
            <td id="orderCode">${item.orderCode}</td>
            <td id="itemName">${item.itemName}</td>
            <td id="qty">${item.qty}</td>
            <td id="leadtime">${item.leadtime}</td>
            <td>협력업체</td>
            <td id="status">
                <select class="status-select" data-order-code="${item.orderCode}">
                    <option value="발주완료" ${item.status === '발주완료' ? 'selected' : ''}>발주완료</option>
                    <option value="검수진행중" ${item.status === '검수진행중' ? 'selected' : ''}>검수진행중</option>
                    <option value="검수마감" ${item.status === '검수마감' ? 'selected' : ''}>검수마감</option>
                </select>
            </td>
        </tr>
    `;
}

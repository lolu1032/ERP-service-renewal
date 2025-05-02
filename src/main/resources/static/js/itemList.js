$(document).ready(function() {
    let selectItem = [];
    let quoData = [];
    let checkData = false;
    let totalSum = 0; // 총합 가격을 저장할 변수

    // .check-item에 대한 change 이벤트를 바인딩
    $('.check-item').off('change').on('change', function() {
        const row = $(this).closest('tr');
        const item_id = row.find('[data-item_id]').text();
        const item_code = row.find('[data-item_code]').text();
        const name = row.find('[data-item_name]').text();
        const cons_qty = row.find('[data-qty]').text();
        const cons_loc = row.find('[data-cons_loc]').text();
        const cons_date = row.find('[data-cons_date]').text();
        const spec = row.find('[data-spec]').text();
        const price = parseFloat(row.find('[data-price]').text()); // 가격을 숫자로 변환
        const type = row.find('[data-type]').text();

        if (this.checked) {
            selectItem = []; // 값 초기화
            $('.check-item').not(this).prop('checked', false); // 다른 체크박스는 체크 해제

            selectItem.push({ item_id, item_code, name, cons_qty, cons_loc, cons_date, spec, price, type });
            checkData = true;
        } else {
            selectItem = selectItem.filter(item => item.item_id !== item_id); // 선택 해제 시 해당 항목 제거
            checkData = false;
        }
    });

    // 견적서 보기
    $('#quotationBtn').on('click', function() {
        updateTable(selectItem);
    });

    $('#quoCreate').on('click', function() {
        quoData = {
            item_id: $("#item_id").text(),
            price: $("#price").text(),
            total_amount: $("#total_amount").text(),
        };

        console.log(quoData);

        $.ajax({
            url: '/quotation/quoCreate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(quoData),
            success: function(response) {
                alert("견적서가 추가되었습니다.");
                $('#newItemModal').modal('hide');
            },
            error: function(xhr, status, error) {
                alert("견적서 추가에 실패했습니다: " + (xhr.status ? xhr.status + ': ' + xhr.statusText : error));
            }
        });
    });
});

// 테이블 업데이트 함수
function updateTable(items) {
    const tableBody = $('#quotationTableBody');
    tableBody.empty(); // 테이블 내용 초기화
    totalSum = 0; // 총합 초기화

    items.forEach(item => {
        const res = createTableRow(item);
        tableBody.append(res); // 테이블에 행 추가
    });

    tableBody.append(addTotalRow()); // 총합 가격 추가
}

// 테이블 행 생성 함수
function createTableRow(item) {
    let leadtime = getRandomDate();
    let total_amount = item.price * item.cons_qty; // 각 항목의 총금액 계산
    totalSum += total_amount; // 총합 가격에 더하기

    return `
        <tr>
            <td id="item_id" hidden>${item.item_id}</td>
            <td>${item.name}</td>
            <td style="color: red;">${item.cons_qty}</td>
            <td id="price">${item.price}</td>
            <td id="total_amount">${total_amount}</td>
            <td>${leadtime}</td>
        </tr>
    `;
}

// 테이블의 마지막에 총합 가격을 추가하는 함수
function addTotalRow() {
    return `
        <tr>
            <td colspan="4" style="text-align: center;">총 합계</td>
            <td style="color: red;">${totalSum}</td>
        </tr>
    `;
}

function getRandomDate() {
    const today = new Date();
    const randomDays = Math.floor(Math.random() * (5 - 2 + 1)) + 2;
    const randomDate = new Date(today);
    randomDate.setDate(today.getDate() + randomDays);
    const year = String(randomDate.getFullYear()).slice(-2);
    const month = String(randomDate.getMonth() + 1).padStart(2, '0');
    const day = String(randomDate.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
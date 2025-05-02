$(document).ready(function() {
    $('.modal-btn').click(function() {
        // 클릭된 버튼의 부모 행을 찾음
        var row = $(this).closest('tr');

        // 행의 각 데이터 값을 추출
        var id = row.find('td:nth-child(1)').text();
        var recDate = row.find('td:nth-child(2)').text();
        var supName = row.find('td:nth-child(3)').text();
        var invenName = row.find('td:nth-child(4)').text();
        var itemName = row.find('td:nth-child(5)').text();
        var qty = row.find('td:nth-child(6)').text();

        // 모달 테이블에 데이터를 삽입
        var tableRow = `
            <tr>
                <td id="numVal">${id}</td>
                <td>${recDate}</td>
                <td>${supName}</td>
                <td>${invenName}</td>
                <td>${itemName}</td>
                <td>${qty}</td>
                <td>
                    <select id="selectVal" class="form-control" name="inspectionStatus">
                        <option value="진행중">진행중</option>
                        <option value="완료">완료</option>
                     </select>
                </td>
                <input type="hidden" name="numVal" value="${id}">
            </tr>`;
        // 모달의 테이블에 데이터 추가
        $('.modal-table').html(tableRow);
    });
})
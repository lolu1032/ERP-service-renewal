// 검색 date
function getTodayDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 +1
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

$('#DataInput, #DeliveryDataInput, #date, #deadline').val(getTodayDate());

$('.dateInput .date, .deliveryDateInput .date, .planDateInput .date, .DVPlanDateInput .date').datepicker({
    format: "yyyy-mm-dd",
    language: "ko",
    todayBtn: 'linked',
    todayHighlight: true,
    autoclose: true,
});
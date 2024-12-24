let calendar;
let currentEvent = null;
let selectedDate;

function showToast(message, duration = 3000) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.style.display = 'block';
    
    setTimeout(() => {
        toast.style.display = 'none';
    }, duration);
}

document.addEventListener('DOMContentLoaded', function() {
    const calendarEl = document.getElementById('calendar');

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'he',
        direction: 'rtl',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        editable: true,
        selectable: true,
        dayMaxEvents: true,
        
        select: function(info) {
            // מצב יצירת אירוע חדש
            currentEvent = null;
            document.getElementById('modalTitle').textContent = 'הוסף אירוע חדש';
            document.getElementById('submitBtn').textContent = 'הוסף אירוע';
            document.getElementById('deleteBtn').style.display = 'none';
            document.getElementById('eventForm').reset();
            
            selectedDate = info.start;
            document.getElementById('eventModal').style.display = 'block';
        },
        
        eventClick: function(info) {
            // מצב עריכת אירוע קיים
            currentEvent = info.event;
            document.getElementById('modalTitle').textContent = 'ערוך אירוע';
            document.getElementById('submitBtn').textContent = 'עדכן אירוע';
            document.getElementById('deleteBtn').style.display = 'inline-flex';
            
            // מילוי הטופס בנתונים הקיימים
            document.getElementById('eventTitle').value = currentEvent.title;
            document.getElementById('eventStart').value = currentEvent.start.toTimeString().slice(0,5);
            document.getElementById('eventEnd').value = currentEvent.end.toTimeString().slice(0,5);
            document.getElementById('eventType').value = currentEvent.classNames[0];
            
            document.getElementById('eventModal').style.display = 'block';
        },

        eventDrop: function(info) {
            showToast('האירוע הועבר בהצלחה');
        },
        
        eventResize: function(info) {
            showToast('משך האירוע עודכן בהצלחה');
        }
    });

    // Form submission handler
    document.getElementById('eventForm').onsubmit = function(e) {
        e.preventDefault();
        
        const title = document.getElementById('eventTitle').value;
        const startTime = document.getElementById('eventStart').value;
        const endTime = document.getElementById('eventEnd').value;
        const eventType = document.getElementById('eventType').value;

        const eventDate = currentEvent ? currentEvent.start : selectedDate;
        const startDate = new Date(eventDate);
        const endDate = new Date(eventDate);
        
        const [startHours, startMinutes] = startTime.split(':');
        const [endHours, endMinutes] = endTime.split(':');
        
        startDate.setHours(parseInt(startHours), parseInt(startMinutes));
        endDate.setHours(parseInt(endHours), parseInt(endMinutes));

        if (currentEvent) {
            // עדכון אירוע קיים
            currentEvent.setProp('title', title);
            currentEvent.setStart(startDate);
            currentEvent.setEnd(endDate);
            currentEvent.setProp('classNames', [eventType]);
            showToast('האירוע עודכן בהצלחה');
        } else {
            // יצירת אירוע חדש
            calendar.addEvent({
                title: title,
                start: startDate,
                end: endDate,
                className: eventType
            });
            showToast('האירוע נוצר בהצלחה');
        }

        document.getElementById('eventModal').style.display = 'none';
    };

    // קוד JavaScript להוספת הטוסט

document.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('toast');
    const eventForm = document.getElementById('eventForm');
    
    // אירוע יצירת אירוע חדש
    eventForm.addEventListener('submit', function (e) {
        e.preventDefault();
        
        // כאן תוכל להוסיף את הקוד להוספת האירוע ללוח השנה
        
        // לאחר הוספת האירוע, הצגת הטוסט
        toast.classList.add('show');
        toast.textContent = "נוצר בהצלחה"; // טקסט הטוסט
        
        // הסתרת הטוסט לאחר 3 שניות
        setTimeout(function () {
            toast.classList.remove('show');
        }, 3000);
    });
});


    // Delete button handler
    document.getElementById('deleteBtn').onclick = function() {
        if (currentEvent && confirm('האם אתה בטוח שברצונך למחוק את האירוע?')) {
            currentEvent.remove();
            document.getElementById('eventModal').style.display = 'none';
            showToast('האירוע נמחק בהצלחה');
            currentEvent = null;
        }
    };

    // סגירת מודאל
    document.querySelector('.close').onclick = function() {
        document.getElementById('eventModal').style.display = 'none';
    };

    window.onclick = function(event) {
        if (event.target.classList.contains('modal')) {
            document.getElementById('eventModal').style.display = 'none';
        }
    };

    calendar.render();

    // הוספת אירועי דוגמה
    calendar.addEventSource([
        {
            title: 'פגישת צוות',
            start: '2024-12-25T10:00:00',
            end: '2024-12-25T12:00:00',
            className: 'event-work'
        },
        {
            title: 'אירוע משפחתי',
            start: '2024-12-27T15:00:00',
            end: '2024-12-27T18:00:00',
            className: 'event-personal'
        },
        {
            title: 'דדליין חשוב',
            start: '2024-12-29T09:00:00',
            end: '2024-12-29T18:00:00',
            className: 'event-important'
        }
    ]);
});

$('.navTrigger').click(function () {
    $(this).toggleClass('active');
    console.log("Clicked menu");
    $("#mainListDiv").toggleClass("show_list");
    $("#mainListDiv").fadeIn();

});

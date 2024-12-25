let currentEvent = null;
let selectedDate;

// פונקציות חדשות לתקשורת עם ה-API
async function fetchEvents() {
    const response = await fetch('http://localhost:8080/api/events');
    const events = await response.json();
    return events.map(event => ({
        id: event.eventId,
        title: event.title,
        start: new Date(event.startDate),
        end: new Date(event.endDate),
        className: 'event-' + event.status.toLowerCase()
    }));
}

async function createEvent(eventData) {
    const response = await fetch('http://localhost:8080/api/events', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData)
    });
    return await response.json();
}

async function updateEvent(eventId, eventData) {
    const response = await fetch(`http://localhost:8080/api/events/${eventId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData)
    });
    return await response.json();
}

async function deleteEvent(eventId) {
    await fetch(`http://localhost:8080/api/events/${eventId}`, {
        method: 'DELETE'
    });
}

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

    const calendar = new FullCalendar.Calendar(calendarEl, {
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
        events: fetchEvents, // שימוש בפונקציה החדשה לטעינת אירועים

        select: function(info) {
            currentEvent = null;
            document.getElementById('modalTitle').textContent = 'הוסף אירוע חדש';
            document.getElementById('submitBtn').textContent = 'הוסף אירוע';
            document.getElementById('deleteBtn').style.display = 'none';
            document.getElementById('eventForm').reset();

            selectedDate = info.start;
            document.getElementById('eventModal').style.display = 'block';
        },

        eventClick: function(info) {
            currentEvent = info.event;
            document.getElementById('modalTitle').textContent = 'ערוך אירוע';
            document.getElementById('submitBtn').textContent = 'עדכן אירוע';
            document.getElementById('deleteBtn').style.display = 'inline-flex';

            document.getElementById('eventTitle').value = currentEvent.title;
            document.getElementById('eventStart').value = currentEvent.start.toTimeString().slice(0,5);
            document.getElementById('eventEnd').value = currentEvent.end.toTimeString().slice(0,5);
            document.getElementById('eventType').value = currentEvent.classNames[0];

            document.getElementById('eventModal').style.display = 'block';
        },

        eventDrop: async function(info) {
            try {
                await updateEvent(info.event.id, {
                    title: info.event.title,
                    startDate: info.event.start,
                    endDate: info.event.end
                });
                showToast('האירוע הועבר בהצלחה');
            } catch (error) {
                console.error('שגיאה בעדכון האירוע:', error);
                info.revert();
                showToast('שגיאה בעדכון האירוע');
            }
        },

        eventResize: async function(info) {
            try {
                await updateEvent(info.event.id, {
                    startDate: info.event.start,
                    endDate: info.event.end
                });
                showToast('משך האירוע עודכן בהצלחה');
            } catch (error) {
                console.error('שגיאה בעדכון משך האירוע:', error);
                info.revert();
                showToast('שגיאה בעדכון משך האירוע');
            }
        }
    });

    document.getElementById('eventForm').onsubmit = async function(e) {
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

        const eventData = {
            title,
            startDate,
            endDate,
            status: eventType.replace('event-', '').toUpperCase()
        };

        try {
            if (currentEvent) {
                await updateEvent(currentEvent.id, eventData);
                currentEvent.remove();
                calendar.addEvent({
                    id: currentEvent.id,
                    title: eventData.title,
                    start: eventData.startDate,
                    end: eventData.endDate,
                    className: eventType
                });
                showToast('האירוע עודכן בהצלחה');
            } else {
                const newEvent = await createEvent(eventData);
                calendar.addEvent({
                    id: newEvent.eventId,
                    title: newEvent.title,
                    start: new Date(newEvent.startDate),
                    end: new Date(newEvent.endDate),
                    className: eventType
                });
                showToast('האירוע נוצר בהצלחה');
            }
        } catch (error) {
            console.error('שגיאה בשמירת האירוע:', error);
            showToast('שגיאה בשמירת האירוע');
        }

        document.getElementById('eventModal').style.display = 'none';
    };

    document.getElementById('deleteBtn').onclick = async function() {
        if (currentEvent && confirm('האם אתה בטוח שברצונך למחוק את האירוע?')) {
            try {
                console.log(`שולח בקשת מחיקה לאירוע עם ID: ${currentEvent.id}`);
                await deleteEvent(currentEvent.id);
                console.log(`אירוע עם ID: ${currentEvent.id} נמחק בהצלחה`);
                currentEvent.remove();
                showToast('האירוע נמחק בהצלחה');
            } catch (error) {
                console.error('שגיאה במחיקת האירוע:', error);
                showToast('שגיאה במחיקת האירוע');
            }
            document.getElementById('eventModal').style.display = 'none';
            currentEvent = null;
        }
    };

    document.querySelector('.close').onclick = function() {
        document.getElementById('eventModal').style.display = 'none';
    };

    window.onclick = function(event) {
        if (event.target.classList.contains('modal')) {
            document.getElementById('eventModal').style.display = 'none';
        }
    };

    calendar.render();
});

$('.navTrigger').click(function () {
    $(this).toggleClass('active');
    console.log("Clicked menu");
    $("#mainListDiv").toggleClass("show_list");
    $("#mainListDiv").fadeIn();
});
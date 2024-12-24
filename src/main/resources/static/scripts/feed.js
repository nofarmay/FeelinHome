// $(document).ready(function(){
//     setTimeout(function() {
//       $('.placeholder').hide();
//       $('.post').not('.placeholder').show();
//     },1300)
//   });

// פונקציה להוספת פוסט חדש
function addPost() {
    const postContainer = document.getElementById('feed-container');
  
    const newPost = document.createElement('div');
    newPost.classList.add('post');
  
    newPost.innerHTML = `
        <h3>כותרת הפוסט החדשה</h3>
        <p>תוכן הפוסט החדשה...</p>
        <button class="like-button">אהבתי</button>
        <button class="comment-button">הוסף תגובה</button>
        <div class="comments-section">
            <input type="text" class="comment-input" placeholder="הוסף תגובה...">
            <button class="submit-comment">שלח</button>
            <div class="comments">
                <div class="comment">תגובה 1</div>
                <div class="comment">תגובה 2</div>
            </div>
        </div>
    `;
  
    postContainer.appendChild(newPost);
  }
  
  // פונקציה לשליחת תגובה לפוסט
  document.addEventListener('click', function (event) {
    if (event.target && event.target.classList.contains('submit-comment')) {
        const commentInput = event.target.closest('.comments-section').querySelector('.comment-input');
        const commentsDiv = event.target.closest('.comments-section').querySelector('.comments');
  
        const newComment = document.createElement('div');
        newComment.classList.add('comment');
        newComment.innerText = commentInput.value;
  
        commentsDiv.appendChild(newComment);
        commentInput.value = ''; // לנקות את שדה התגובה
    }
  });
  
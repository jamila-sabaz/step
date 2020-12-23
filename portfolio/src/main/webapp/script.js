// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */

//Create a boolean flag to note when the button for random show and random fact have been 
//clicked so they will open on first click and close on the second click
showHasBeenClicked = false;
factHasBeenClicked = false;

function recommendRandomShow() {
  //options for randomiser
  const shows =
    ['The Good Place', 'Gossip Girl', 'Community', 'Emily in Paris', 'How I met Your Mother'];
    
  // Pick a random tv show.
  const show = shows[Math.floor(Math.random() * shows.length)];

  // Add it to the page.
  const showContainer = document.getElementById('tv-show-container');
  //check if the button has been clicked already and if yes collapse container
  if (showHasBeenClicked === true){
    showContainer.innerText = null;
    showHasBeenClicked = false;
  }
  //if not show container and flag it as clicked  
  else{
    showContainer.innerText = show;
    showHasBeenClicked = true;
  } 
}
/**
 * Adds a random fact to the page.
 */
function randomFact() {
  //options for randomiser
    const facts = 
      ['favourite color - Purple', 'prefers dogs over cats', 'favourite ice cream flavor - strawberry', 
      'favourite singer - Lady Gaga', 'still dreams to become an astronaut', '90% of times remembers her dreams'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  //check if clicked and if so show nothinhg
    if (factHasBeenClicked === true){
      factContainer.innerText = null;
      factHasBeenClicked = false;
    }
    //else if not clicked show the container and flag as clicked
    else{
      factContainer.innerText = fact;
      factHasBeenClicked = true;
  }
}

//Button for 3 sections about me
function openSection(id) {
  var section = document.getElementById(id);
  //check if class of the container is not w3-show (it is hide) 
  //then add class name to show
  if (section.className.indexOf("w3-show") == -1) {
    section.className += " w3-show";
  } 
  //if it is already on show, remove w3-show class to null so it will be hidden
  else {
    section.className = section.className.replace(" w3-show", "");
  } 
}
/**
 * Slideshow 
 */
//Using event listener check if DOM content of the page has been loaded completely 
//only after it is loaded function showSlides is called
document.addEventListener('DOMContentLoaded', () => {
showSlides(0);
});

//this function makes button with arrows move to the Next and Previous slides
function plusSlides(n) {
  showSlides(slideIndex += n);
}

//this function binds dots indicators with the slides to control them
function currentSlide(n) {
  showSlides(slideIndex = n);
}

//this function is main, it takes initial counter and manages slides 
function showSlides(n) {
  let i; //index for 'for' loops
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");
  //check if counter of slides is exceeding number of slider and if yes, reset slide index to 1
  if (n > slides.length) {slideIndex = 1}    
  //if counter=0 set slide index to max number of slides (length =3)
  if (n < 1) {slideIndex = slides.length}
    //initially all slides are hidden (show none)
  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none";  
  }
  //and replace all dots form active to NULL to display inactive mode
  for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(" active", "");
  }
  //brings the slide back (makes it visible)
  slides[slideIndex-1].style.display = "block";  
  //the dot becomes 'active' (indicator is brighter)
  dots[slideIndex-1].className += " active";
}

/**
* Fetch data from the server.
*/
function getServerData() {
  fetch('/data')
    .then((response) => response.text())
    .then((myJSON) => {
      document.getElementById('data-container').innerText = myJSON;
    });
}

// function getNumberOfComments(){
//   var result = +document.getElementById("limit").value;
//   return result;
// }
/** Fetches comments from the server and adds them to the DOM. */
function loadComments() {
  fetch(`/data?limit=${document.getElementById("limit").value}`)
    .then(response => response.json())
    .then((comments) => {
      const commentListElement = document.getElementById('comment-list');
      comments.forEach((comment) => {
        commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

/** Creates an element that represents a comment, excluding its delete button. */
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';
  // The variable title is the content of the comment message.
  const titleElement = document.createElement('span');
  titleElement.innerText = comment.title;

  document.getElementById("refresh").addEventListener('click', () => {
    // Once refresh button is clicked, remove the comments from the DOM.
    commentElement.remove();
  });

  document.getElementById("delete-comments").addEventListener('click', () => {
    deleteAllComment(comment);
    // Remove the comment from the DOM.
    commentElement.remove();
  });
  
  commentElement.appendChild(titleElement);
  return commentElement;
}

/** Tells the server to delete all comments. */
function deleteAllComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-data', {method: 'POST', body: params});
}

/** Creates a map and adds it to the page. */
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      //{center: {lat: 	43.238949, lng: 76.889709}, zoom: 16});
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});
}

function loadStuff(value){
  createMap();
  loadComments(value);
}

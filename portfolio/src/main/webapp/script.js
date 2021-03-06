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

//Create a boolean flag to note when the button for random show and random fact have been 
//clicked so they will open on first click and close on the second click
showHasBeenClicked = false;
factHasBeenClicked = false;
/**
 * Adds a random tv show suggestion to the page.
 */
function recommendRandomShow() {
  // Options for randomiser.
  const shows =
    ['The Good Place', 'Gossip Girl', 'Community', 'Emily in Paris', 'How I met Your Mother'];
    
  // Pick a random tv show.
  const show = shows[Math.floor(Math.random() * shows.length)];

  // Add it to the page.
  const showContainer = document.getElementById('tv-show-container');
  // Check if the button has been clicked already and if yes collapse container.
  if (showHasBeenClicked === true){
    showContainer.innerText = null;
    showHasBeenClicked = false;
  }
  // If not show container and flag it as clicked.
  else{
    showContainer.innerText = show;
    showHasBeenClicked = true;
  }  
}
/**
 * Adds a random fact to the page.
 */
function randomFact() {
  // Options for randomiser.
  const facts = 
  ['favourite color - Purple', 'prefers dogs over cats', 'favourite ice cream flavor - strawberry', 
  'favourite singer - Lady Gaga', 'still dreams to become an astronaut', '90% of times remembers her dreams'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  // Check if clicked and if so show nothinhg.
  if (factHasBeenClicked === true){
    factContainer.innerText = null;
    factHasBeenClicked = false;
  }
  // Else if not clicked show the container and flag as clicked.
  else{
    factContainer.innerText = fact;
    factHasBeenClicked = true;
  }
}
/**
 * Button for 3 sections #about-me.
 */
function openSection(id) {
  var section = document.getElementById(id);
  // Check if class of the container is not w3-show (it is hide).
  // Then add class name to show.
  if (section.className.indexOf("w3-show") == -1) {
    section.className += " w3-show";
  } 
  // If it is already on show, remove w3-show class to null so it will be hidden.
  else {
    section.className = section.className.replace(" w3-show", "");
  } 
}
/**
 * Slideshow 
 */
// Using event listener check if DOM content of the page has been loaded completely.
// Only after it is loaded function showSlides is called.
document.addEventListener('DOMContentLoaded', () => {
showSlides(0);
});

// This function makes button with arrows move to the Next and Previous slides.
function plusSlides(n) {
  showSlides(slideIndex += n);
}

//This function binds dots indicators with the slides to control them.
function currentSlide(n) {
  showSlides(slideIndex = n);
}

// This function is main, it takes initial counter and manages slides. 
function showSlides(n) {
  let i; // Index for 'for' loops.
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");
  // Check if counter of slides is exceeding number of slider and if yes, reset slide index to 1.
  if (n > slides.length) {slideIndex = 1}    
  // If counter=0 set slide index to max number of slides (length =3).
  if (n < 1) {slideIndex = slides.length}
    // Initially all slides are hidden (show none).
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  // And replace all dots form active to NULL to display inactive mode.
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  // Brings the slide back (makes it visible).
  slides[slideIndex-1].style.display = "block";  
  // The dot becomes 'active' (indicator is brighter).
  dots[slideIndex-1].className += " active";
}

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
showHasBeenClicked = false;
factHasBeenClicked = false;
function recommendRandomShow() {
  const shows =
    ['The Good Place', 'Gossip Girl', 'Community', 'Emily in Paris', 'How I met Your Mother'];
    
  // Pick a random tv show.
  const show = shows[Math.floor(Math.random() * shows.length)];

  // Add it to the page.
  const showContainer = document.getElementById('tv-show-container');
  
  if (showHasBeenClicked === true){
    showContainer.innerText = null;
    showHasBeenClicked = false;
  }
    
  else{
      showContainer.innerText = show;
      showHasBeenClicked = true;
  }
    
}
/**
 * Adds a random fact to the page.
 */
function randomFact() {
  const facts =
    ['favourite color - Purple', 'prefers dogs over cats', 'favourite ice cream flavor - strawberry', 
    'favourite singer - Lady Gaga', 'still dreams to become an astronauts', '90% of times remembers her dreams'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
    if (factHasBeenClicked === true){
      factContainer.innerText = null;
      factHasBeenClicked = false;
    }
  else{
      factContainer.innerText = fact;
      factHasBeenClicked = true;
  }
}
//Button
function openSection(id) {
  var section = document.getElementById(id);
  if (section.className.indexOf("w3-show") == -1) {
    section.className += " w3-show";
  } else {
    section.className = section.className.replace(" w3-show", "");
  } 
}
/**
 * Automatic Slideshow - change image every 3 seconds
 */
document.addEventListener('DOMContentLoaded', () => {
showSlides(0);
});
function plusSlides(n) {
  showSlides(slideIndex += n);
}

function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  var i;
  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  if (n > slides.length) {slideIndex = 1}    
  if (n < 1) {slideIndex = slides.length}
  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none";  
  }
  for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " active";
}
/*
document.addEventListener('DOMContentLoaded',(event) => {
showSlides(0);
});

function showSlides(slideIndex) {
  var i;
  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  slideIndex++;
  if (slideIndex > slides.length) {slideIndex = 1}    
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " active";
  setTimeout(showSlides, 2000); // Change image every 2 seconds
}*/
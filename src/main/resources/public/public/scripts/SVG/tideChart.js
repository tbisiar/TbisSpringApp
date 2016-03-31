// var XMAX = 628;
// var YMAX = 1000;

// // Create path instructions
// var path = [];
// for (var x = 0; x <= XMAX; x++) {
//     var angle = (x / XMAX) * Math.PI * 2;  // angle = 0 -> 2π
//     var y = Math.sin(angle) * (YMAX / 2) + (YMAX / 2);
//     // M = move to, L = line to
//     path.push((x == 0 ? 'M' : 'L') + x + ',' + y);
// }

// // Create PATH element
// var pathEl = document.createElementNS("http://www.w3.org/2000/svg", "path");
// pathEl.setAttribute('d', path.join(' ') );
// pathEl.style.stroke = 'blue';
// pathEl.style.fill = 'none';

// // Add it to svg element
// document.querySelector('svgTest').appendChild(pathEl);

var tideChart = function() {

  var svg = document.getElementById('svgTest').children[0];
  var origin = { //origin of axes
    x: 314,
    y: 500
  };
  var amplitude = 400; // wave amplitude
  var rarity = 1; // point spacing
  var freq = 31.4; // angular frequency
  var phase = 20; // phase angle

  for (var i = -314; i < 314; i++) {
    var line = document.createElementNS("http://www.w3.org/2000/svg", "line");

    line.setAttribute('x1', (i - 1) * rarity + origin.x);
    line.setAttribute('y1', Math.sin(freq * (i - 1 + phase)) * amplitude + origin.y);

    line.setAttribute('x2', i * rarity + origin.x);
    line.setAttribute('y2', Math.sin(freq * (i + phase)) * amplitude + origin.y);

    line.setAttribute('style', "stroke:black;stroke-width:1");

    svg.appendChild(line);
  }


  // Display coordinates
  var SVGmouseTip = d3.select("g.tooltip.mouse");
}


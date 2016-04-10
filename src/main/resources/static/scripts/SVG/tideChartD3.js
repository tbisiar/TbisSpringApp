var tideChartD3 = function(tidesData){

  var width = 1440;
  var height = 500;
  var multiplier = 100;

  // example data: '[{"id":335,"time":"2016-03-27 10:58:00.0","height":3.1,"locationId":64000},{"id":336,"time":"2016-03-27 16:57:00.0","height":0.7,"locationId":64000},{"id":337,"time":"2016-03-27 23:22:00.0","height":3.1,"locationId":64000},{"id":338,"time":"2016-03-28 05:13:00.0","height":0.8,"locationId":64000}]';

  var tideDepthToCm = function( depth ) {
    return height - depth * multiplier;
  };

  var getDayOffset = function( splitDateTime ) {
    var currentDate = new Date(splitDateTime).getTime();
    var offset = parseInt((currentDate - startMillis)/(1000 * 60 * 60 * 24));
    return offset;
  }

  var dateTimeToMinutes = function( tideData ) {
    var splitDateTime = tideData.time.split(" ");
    var splitTime = splitDateTime[1].split(":");
    var dayOffset = getDayOffset(splitDateTime[0]);
    var minutes = parseInt( splitTime[0] ) * 60 + parseInt( splitTime[1] ) + (dayOffset * 24 * 60);
    return minutes;
  }

  var coordId = 'tideChartD3Coords';
  var svgId = 'svgTestD3';
//  document.getElementById(svgId).onmouseover = function(coordId) {
//    displayCoords(coordId);
//  }
//  var coords = d3.select( '#' + coordId );
//  coords.attr('onmouseover', '"displayCoords(' + coordId + ')"');


//  var coords = d3.select("text#svgCoords")
//                    .append("text")
//                    .attr('id', coordId)
//                    .attr('x', '110')
//                    .attr('y', '115');

  displayCoords(coordId, svgId);




  var startMillis = new Date(tidesData[1].time.split(" ")[0]).getTime();
  var minutes = dateTimeToMinutes( tidesData[0] );
  var lineData = [ { "x": minutes, "y": 0} ];

  var dataLength = tidesData.length;
  for (i=0; i<dataLength; i++) {
    // TODO - setup better way to set time into past
    var minutes = dateTimeToMinutes( tidesData[i] );
    var tideHeight = tideDepthToCm( tidesData[i].height );
    lineData.push( { "x": minutes, "y": tideHeight} );
    if(lineData.length > dataLength) {
      lineData.push( { "x" : minutes, "y": 0});
    }
  }

  var svgContainer = d3.select("div#svgTestD3")
                       .append("svg")
                       .attr("width", "100%")
                       .attr("height", "200")
                       .attr("viewBox", "0 0 " + width + " " + height)
                       .attr("preserveAspectRatio", "none")
                       .attr("version", "1.1")
                       .attr("xmlns", "http://www.w3.org/2000/svg")
                       .attr("xmlns:xlink", "http://www.w3.org/1999/xlink");

  // Create gradient def
  // TODO - is there a better way to load this?
  var defs = svgContainer.append("defs");
  var linearGradient = defs.append("linearGradient")
                       .attr("id", "svgGradient")
                       .attr("x1", "0%")
                       .attr("y1", "0%")
                       .attr("x2", "0%")
                       .attr("y2", "10%");
  var stop1 = linearGradient.append("stop")
                       .attr("offset", "0%")
                       .attr("style", "stop-color:rgb(100,150,150);stop-opacity:1");
  var stop2 = linearGradient.append("stop")
                       .attr("offset", "100%")
                       .attr("style", "stop-color:rgb(100,150,150);stop-opacity:0");

  var circles = svgContainer.selectAll("circle")
                            .data(tidesData)
                            .enter()
                            .append("circle");

  var circleAttributes = circles
                         .attr("cx", function(d){
                           var xVar = dateTimeToMinutes( d );
                           return xVar;
                         })
                         .attr("cy", function (d) {
                           var yVar = tideDepthToCm( d.height );;
                           return yVar;
                         })
                         .attr("r", 20)
                         .style("fill", "#fff");

  var lineFunction = d3.svg.line()
                           .x(function(d) {
                             return d.x;
                           })
                           .y(function(d) {
                             return d.y;
                           })
                           .interpolate("monotone");

  var lineAttributes = svgContainer.append("path")
                                   .attr('d', lineFunction(lineData))
                                   .attr("fill", "url(#svgGradient)")
                                   .attr("stroke", "#fff")
                                   .attr("stroke-width", 3);

}


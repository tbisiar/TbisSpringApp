var tideChartD3 = function(tidesData){


  var timeToMinutes = function( timeString ) {
    var splitTime = timeString.split(":");
    var minutes = parseInt( splitTime[0] ) * 60 + parseInt( splitTime[1] );
    return minutes;
  };

  var tideDepthToCm = function( depth ) {
    return height - depth * multiplier;
  };
  // var svgContainer = document.getElementById('svgTestD3').children[0];

  // var tidesData = [-0.91299251927959, 1.0049121683629985,-0.27793326004093283, 0.07051110184971858,-0.9679999715685375, 1.1495536133887616];
  var dataLength = tidesData.length;
  var width = 1440;
  var height = 500;
  var multiplier = 100;
  var lineData = [ { "x": 0, "y": 0} ];

  for (i=0; i<dataLength; i++) {
    var minutes = timeToMinutes( tidesData[i].x );
    var tideHeight = tideDepthToCm( tidesData[i].y );
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

  var circles = svgContainer.selectAll("circle")
                            .data(tidesData)
                            .enter()
                            .append("circle");

  var circleAttributes = circles
                         .attr("cx", function(d){
                           var xVar = timeToMinutes( d.x );
                           return xVar;
                         })
                         .attr("cy", function (d) {
                           var yVar = tideDepthToCm( d.y );;
                           return yVar;
                         })
                         .attr("r", 20)
                         .style("fill", "red");

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
                                   .attr("fill", "#eee")
                                   .attr("stroke", "blue")
                                   .attr("stroke-width", 1);

  var coordId = 'tideChartD3Coords'
  var coords = d3.select("div#svgTestD3")
                    .append("text")
                    .attr('id', coordId)
                    .attr('x', '110')
                    .attr('y', '115');
  displayCoords(coordId);

  // $(document).ready(function(){
  //     $('[data-toggle="tooltip"]').tooltip();
  // });
}
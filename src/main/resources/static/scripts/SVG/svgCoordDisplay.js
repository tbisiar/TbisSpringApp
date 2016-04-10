
// Script for displaying coordinates of SVG element
var displayCoords = function( coordElementName, svgId ) {

  var timeSpan = 60;
  var heightMultipler = 100;
  var maxHeight = 500;
  var coords = d3.select( '#' + coordElementName );//document.getElementById( coordElementName );
  // var coordsText = coords.getElementsByTagName( 'text' )[0];

  var IE = document.all ? true : false;

  if (!IE) { document.captureEvents( Event.MOUSEMOVE) }
  var svg = document.getElementById( svgId );
  svg.onmousemove = getMouseXY;

  function getMouseXY( e ) {

    var tempX = 0
    var tempY = 0

    if (IE) {
      tempX = event.clientX + document.body.scrollLeft
      tempY = event.clientY + document.body.scrollTop
    } else {
      tempX = e.pageX
      tempY = e.pageY
    }

    coords.text(function(d) { return getSvgCoords(tempX, tempY)});

	return true
  }

    var addZeroIfSingleDigit = function( minutes ) {
      if( minutes < 10 ) {
        return "0" + minutes;
      } else {
        return minutes;
      }
    }

  function getSvgCoords(origX, origY) {

  	// Get the svg element, assuming it is first item in div
    var svgDiv = document.getElementById( 'svgTestD3' );

    if(svgDiv) {
      var svgEl = svgDiv.getElementsByTagName( 'svg' )[0];

      var viewBox = new String( svgEl.getAttribute( 'viewBox' ) ).split( ' ' );

      var rect = svgEl.getBoundingClientRect();
      var offsetX = rect.left;
      var offsetY = rect.top;

      var pixWidth = getElementWidth(svgEl);
      var pixHeight = getElementHeight(svgEl);

      var yOriginOnScreen = offsetY + pixHeight;

      var pixelWidthRatio = viewBox[2] / pixWidth;
      var pixelHeightRatio = viewBox[3] / pixHeight;

      // var offset = getElementOffset( svgEl );
      // var offsetX = offset.left;
      // var offsetY = offset.top;

      var convertedX = (origX - offsetX) * pixelWidthRatio;
      var convertedY = (yOriginOnScreen - origY) * pixelHeightRatio;

      dateTimeX = Math.floor(convertedX / timeSpan) + ":" + addZeroIfSingleDigit( Math.floor(convertedX % timeSpan) );
//      depthY = ((maxHeight - (convertedY * heightMultipler)) / heightMultipler).toFixed(2) + " cm ";

//        var dispY = offsetY - origY + viewBox[3];

      var bodyRect = document.body.getBoundingClientRect();
      var dispY = ((rect.bottom - bodyRect.top - origY) * pixelHeightRatio / heightMultipler).toFixed(2) + " m";

      return [ dateTimeX, dispY ];
    }


    // function getElementOffset( el ) {
    //   var _x = 0;
    //   var _y = 0;
    //   while( el && !isNaN( el.offsetLeft ) && !isNaN( el.offsetTop ) ) {
    //       _x += el.offsetLeft - el.scrollLeft;
    //       _y += el.offsetTop - el.scrollTop;
    //       el = el.offsetParent;
    //   }
    //   return { top: _y, left: _x };
    // }


    function getElementWidth( el ) {
      var width = 0;
      if( el && !isNaN( el.clientWidth )) {
        width += el.clientWidth;
      }
      return width;
    }


    function getElementHeight( el ) {
      var height = 0;
      if( el && !isNaN( el.clientHeight )) {
        height += el.clientHeight;
      }
      return height;
    }
  }
}
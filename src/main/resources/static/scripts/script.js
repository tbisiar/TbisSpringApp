// script.js

// Let's do this in strict mode to encourage good habits :)
"use strict";

// Initialize the angular module
var tbAppMod = angular.module('tbApp', ['ngRoute']);

// Configure the routes
tbAppMod.config(function($routeProvider) {
// tbAppMod.config(function($routeProvider, $locationProvider) {
  $routeProvider

  .when('/', {
  	templateUrl: 'pages/home.html',
  	controller: 'mainController'
  })

  .when('/about', {
  	templateUrl: 'pages/about.html',
  	controller: 'aboutController'
  })

  .when('/jsonComparator', {
  	templateUrl: 'pages/jsonComparator.html',
  	controller: 'jsonComparatorController'
  })

  .when('/tideChart', {
  	templateUrl: 'pages/tideChart.html',
  	controller: 'tideChartController'
  })

  .when('/contact', {
  	templateUrl: 'pages/contact.html',
  	controller: 'contactController'
  });

  // $locationProvider.html5Mode(true);

})

// Initialize the controller
tbAppMod.controller('mainController', function($scope) {
  $scope.message = 'Hello, I am Terry Bisiar and this is my portfolio.';
});

tbAppMod.controller('aboutController', function($scope) {
  $scope.message = 'Let me tell you a little about myself';
});

tbAppMod.controller('jsonComparatorController', function($scope) {
  $scope.message = 'An example for an AngularJS front end.';
});

tbAppMod.controller('tideChartController', function($scope, $http) {
  $scope.message = 'An example for a SVG GUI to display tide at an Auckland Harbor location.';

  // Load TideData into $scope.tideData
  var tideData = TideDataCtrlAjax($scope, $http);

  // Next round test data (matching JSON format from api call)
  tideData = [{
                "id":333,
                "time":"2016-03-26 22:46:00.0",
                "height":3.1,
                "locationId":64000
              },{
                "id": 334,
                "time": "2016-03-27 04:35:00.0",
                "height": 0.8,
                "locationId": 64000
              },{
                "id":335,
                "time":"2016-03-27 10:58:00.0",
                "height":3.1,
                "locationId":64000
              },{
                "id":336,
                "time":"2016-03-27 16:57:00.0",
                "height":0.7,
                "locationId":64000
              },{
                "id":337,
                "time":"2016-03-27 23:22:00.0",
                "height":3.1,
                "locationId":64000
              },{
                "id":338,
                "time":"2016-03-28 05:13:00.0",
                "height":0.8,
                "locationId":64000
              }];

  // Load the tideChart with test data specified
  tideChartD3( tideData );
});

tbAppMod.controller('contactController', function($scope) {
  $scope.message = 'Contact ME!';
});

var TideDataCtrlAjax = function($scope, $http) {
    $http({method: 'GET',
              url: 'http://localhost:8080/tideData/next24Hours',
              username: 'user',
              password: 'asdf'})
        .success(
            function(data) {
                $scope.tideData = data.slice(); // response data
            }
        );
}


/* First Angular App */

// Initialize the module
var jcAppModule = new angular.module( 'jcApp', []);

// Configure the module
// jcAppModule.filter('greet', function() {
// 	return function(name) {
// 		return 'Hello, ' + name + '!';
// 	};
// });

// Create controller for initialization
jcAppModule.controller('qtyCostController', ['$scope', function($scope) {
  $scope.qty = 1;
  $scope.cost = 2;
}]);
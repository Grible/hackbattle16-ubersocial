(function() {
  'use strict';

  angular
    .module('ubersocial')
    .factory('servicename', function($resource, API_URL) {
      var service = {};

      service.queryModels = function() {
        return $resource(API_URL + '/frontend/models').query().$promise;
      };

      service.query = function(modelId) {
        return $resource(API_URL + '/frontend/models/:modelId/taxonomies', { 'modelId': modelId }).query().$promise;
      };

      return service;
    });

})();

(function() {
  'use strict';

  angular
    .module('ubersocial')
    .controller('SampleController', function() {
      var vm = this;


      (function() {

        vm.randomNo = Math.random();

        // taxonomies.queryModels().then(function(models) {
        //   vm.taxonomyModels = models;
        //   vm.selectedTaxonomyModelId = models[0].id;
        //   vm.fetchTaxonomies();
        // }).catch(function() {
        //   $log.error('failed to retrieve taxonomy models');
        // });
      })();

    });

})();

// ContactController.js

function MainController() {
  var vm = this;

  // Initialize the model object
  vm.contact = {};

  // Initialize the form fields
  vm.contactFields = [
    {
      key: 'first_name',
      type: 'input',
      templateOtions: {
        type: 'text',
        label: 'First Name',
        placeholder: 'Enter your first name',
        required: true
      }
    },
    {
      key: 'last_name',
      type: 'input',
      templateOptions: {
        type: 'text',
        label: 'Last Name',
        placeholder: 'Enter your last name',
        required: true
      }
    },
    {
      key: 'email',
      type: 'input',
      templateOptions: {
        type: 'email',
        label: 'Email Address',
        placeholder: 'Enter email',
        required: true
      }
    }

  ]

}
/**
 * JavaScript file that manages the general behavior of the complete web user interface. Also it implements the
 * communication with the server through Ajax (post) requests. Requires JQuery, Bootstrap and Bootstrap-Editable
 * to work properly.
 * <p>
 * Created by Jan on 20.01.2017.
 */

//Holds references and functions for components that are displayed on the user interface
ui = {};

/**
 * Initializes the ui, attaches the javascript to the user interface and fetches general javascript references to
 * ui components. Has to be called when the page has been loaded completely.
 */
ui.init = function () {
    //Get reference of the container in which the alerts are displayed
    Alert.container = $('#alert_container');
    //Get references of the table's components in which the rules are displayed
    ui.rule_table.container = $('#rule_table')
    ui.rule_table.body = ui.rule_table.container.find('tbody');
    //Get references to the form with which rules can be added
    ui.add_rule.form = document.getElementById('add_rule_form');
    ui.add_rule.submit = $('#add_rule_submit');
    //Get references of the table's components in which the logs are displayed
    ui.log_table.container = $('#log_table');
    ui.log_table.body = ui.log_table.container.find('tbody');
    //Initialize the corresponding form
    ui.add_rule.init();
    //Get button references of the main control
    ui.main_control.restart = $('#button_control_restart');
    ui.main_control.shutdown = $('#button_control_shutdown');
    //Initialize the functionality of the buttons
    ui.main_control.init();
    //Start log polling
    ui.log_table.startPolling();
};

//Holds necessary elements and functions to display a confirm dialog on the user interface with which the user can make
//a decision
Confirm = {};

/**
 * Opens a new confirm dialog with which the user has to make a decision (Yes/No). Depending on the user's choice
 * different actions can be performed. In case of that the user clicks on the 'X' at the top-left corner of the dialog,
 * no action will be performed.
 * @param head The title on the top of the dialog
 * @param content The content message that describes the decision to make
 * @param yes Callback function that is called when the user presses the 'Yes' button of the dialog
 * @param no Callback function that is called when the user presses the 'No' button of the dialog
 * @param yes_type The bootstrap type of the 'yes' button. Affects the style of the button and has to be one of the
 * following strings: 'success', 'info', 'warning', 'danger'
 * @param no_type The bootstrap type of the 'no' button. Affects the style of the button and has to be one of the
 * following strings: 'success', 'info', 'warning', 'danger'
 */
Confirm.show = function (head, content, yes, no, yes_type, no_type) {

    //Create bootstrap modal dialog without buttons
    var modal = $('<div class="modal fade" id="myModal" role="dialog"><div class="modal-dialog">' +
        '<div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal">' +
        '&times;</button><h4 class="modal-title">' + head + '</h4></div>' +
        '<div class="modal-body">' + content + '</div><div class="modal-footer"></div></div></div></div>');

    //Create the 'yes' button
    var yes_button = $('<button type="button" class="btn btn-' + yes_type + '" data-dismiss="modal">Yes</button>');
    yes_button.click(yes);

    //Create the 'no' button.
    var no_button = $('<button type="button" class="btn btn-' + no_type + '" data-dismiss="modal">No</button>');
    no_button.click(no);

    //Append the buttons to the dialog
    modal.find('.modal-footer').append(yes_button);
    modal.find('.modal-footer').append(no_button);

    //Create the modal dialog
    modal.modal();
};

//Holds necessary elements and functions to display alert messages on the user interface
Alert = {};
//Reference to the container in which the alerts are displayed
Alert.container = null;

/**
 * Shows a new alert in the alert container on the top of the page. Alerts are stackable (many alerts can be
 * displayed at the same time) and disappear after five seconds. Also they can be closed manually.
 * @param type The bootstrap type of the alert to show. Has to be one of the following strings: 'success', 'info',
 * 'warning', 'danger'
 * @param title The title of the alert to display
 * @param text The text message of the alert to display
 */
Alert.show = function (type, title, text) {
    //Create new hidden alert with title and text
    var alert = $('<div></div>');
    alert.hide();
    alert.addClass('alert');
    alert.addClass('alert-' + type);
    alert.append('<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>');
    alert.append('<strong>' + title + '</strong>');
    alert.append(' ');
    alert.append(text);

    //Add the alert to the top of the alert container
    Alert.container.prepend(alert);

    //Hide the alert after five seconds
    alert.fadeTo(5000, 500).slideUp(500, function () {
        alert.slideUp(500);
    });
};

/**
 * Shows a new success alert in the alert container on the top of the page. Alerts are stackable (many alerts can be
 * displayed at the same time) and disappear after five seconds. Also they can be closed manually. A success alert uses
 * the bootstrap 'success' type as style.
 * @param text The success text message of the alert to display
 */
Alert.success = function (msg) {
    Alert.show("success", "Success!", msg);
};

/**
 * Shows a new error alert in the alert container on the top of the page. Alerts are stackable (many alerts can be
 * displayed at the same time) and disappear after five seconds. Also they can be closed manually. A success alert uses
 * the bootstrap 'error' type as style.
 * @param text The error text message of the alert to display
 */
Alert.error = function (msg) {
    Alert.show("danger", "Error:", msg);
};

//Holds the button references of the main control
ui.main_control = {};
//Reference to the restart button
ui.main_control.restart = null;
//Reference to the shutdown button
ui.main_control.shutdown = null;
/**
 * Adds the functionality to the main control buttons
 */
ui.main_control.init = function () {

    //Restart button
    ui.main_control.restart.click(function () {
        //Let the user confirm that he really wants to restart
        Confirm.show("Server restart", "Do you really want to restart the server? During the restart period it will " +
            "not be reachable!", function () {
            //Perform server request in order to restart
            Request.restart(function (data) {
                Alert.success("The server is restarting...");
            }, function (message) {
                Alert.error(message);
            });
        }, function () {
        }, 'danger', 'primary');
    });

    //Shutdown button
    ui.main_control.shutdown.click(function () {
        //Let the user confirm that he really wants to shutdown
        Confirm.show("Server shutdown", "Do you really want to shut down the server? After this operation it will " +
            "not be reachable anymore!", function () {
            //Perform server request in order to shut down
            Request.shutdown(function (data) {
                Alert.success("The server is shutting down...");
            }, function (message) {
                Alert.error(message);
            });
        }, function () {
        }, 'danger', 'primary');
    });
};

//Holds all necessary elements and functions of the rule table so that it is possible to work with it dynamically
ui.rule_table = {};
//Main container of the rule table
ui.rule_table.container = null;
//The 'tbody' of the rule table
ui.rule_table.body = null;

/**
 * Makes the rules in the rule table editable directly using the bootstrap-editable framework.
 */
ui.rule_table.makeEditable = function () {
    //Set the edit mode to inline and disable highlighting
    $.fn.editable.defaults.mode = 'inline';
    $.fn.editable.defaults.highlight = false;

    /**
     * Function that is called when editing of a rule was successful.
     * @param response The server response
     * @param newValue The new value that was set to the edited rule
     */
    var success_function = function (response, newValue) {

        //Parse the json server response
        response = JSON.parse(response);

        //Check if there was a internal server error
        if (response.error) {
            //Error, show error alert and refresh the rules
            Alert.fail(response.msg);
            RuleManager.refresh();
            return;
        }

        //Editing was successful, therefore show a success alert
        Alert.success('The rule was changed successfully regarding the new value "' +
            newValue + '".');
    };

    /**
     * Function that is called when editing of a rule was not successful.
     * @param response The server response
     * @param newValue The new value that should have been set to the concerning rule
     */
    var error_function = function (response, newValue) {
        //Show an error alert
        Alert.fail('The requested service is currently not available. ' +
            'Please try again later.');
    };

    //Make rule names editable
    $('.rule_edit_name').editable({
        type: 'text',
        title: 'Edit the unique rule name',
        name: 'name',
        url: '/edit',
        success: success_function,
        error: error_function
    });

    //Make rule trigger class names editable
    $('.rule_edit_trigger').editable({
        type: 'text',
        title: 'Edit the rule trigger',
        name: 'trigger',
        url: '/edit',
        success: success_function,
        error: error_function
    });

    //Make rule action method names editable
    $('.rule_edit_action').editable({
        type: 'text',
        title: 'Edit the rule action',
        name: 'action',
        url: '/edit',
        success: success_function,
        error: error_function
    });
};

//Holds all necessary elements and functions for managing the rule adding form
ui.add_rule = {};
//The form with which new rules can be added
ui.add_rule.form = null;
//The submit button of the rule adding form
ui.add_rule.submit = null;

/**
 * Initializes the rule adding form. After the call of this function it is possible to add new rules using this form
 * and send them to the server through an ajax request.
 */
ui.add_rule.init = function () {
    //Disable auto validation
    ui.add_rule.form.noValidate = true;
    //Add an event listener for automatic validation of the rule fields
    ui.add_rule.form.addEventListener('submit', function (event) {
        event.preventDefault(); // dismiss the default functionality

        //Are the input fields valid?
        if (!event.target.checkValidity()) {
            //Open error alert
            Alert.error("In order to add a rule all input fields are required and have to be" +
                " filled in.");
            return;
        }
        //Read in the given rule properties
        var fields = $(ui.add_rule.form).serializeArray();

        //Add new rule and send it to the server
        RuleManager.add(fields[0].value, fields[1].value, fields[2].value, (fields[3].value == "Enabled"));
    });
};

ui.log_table = {};
ui.log_table.container = null;
ui.log_table.body = null;
ui.log_table.startPolling = function () {
    function getIcon(type) {
        var icon_strings = new Array();
        icon_strings['ERROR'] = 'remove';
        icon_strings['WARNING'] = 'alert';
        icon_strings['INFO'] = 'bullhorn';
        icon_strings['EVENT_IN'] = 'arrow-right';
        icon_strings['EVENT_OUT'] = 'arrow-left';

        var icon_title = new Array();
        icon_title['ERROR'] = 'Error';
        icon_title['WARNING'] = 'Warning';
        icon_title['INFO'] = 'Information';
        icon_title['EVENT_IN'] = 'Incoming event';
        icon_title['EVENT_OUT'] = 'Outgoing event';

        var icon = $('<span class="glyphicon"></span>');
        icon.addClass('glyphicon-' + icon_strings[type]);

        return icon;
    }

    function pushNotification(notification) {
        var row = $('<tr></tr>');
        row.append($('<td></td>').append(Basics.formatDate(notification.date)));
        row.append($('<td></td>').append(getIcon(notification.type)));
        row.append($('<td style="text-align: left;"></td>').append(notification.message));
        ui.log_table.body.append(row);
    }

    function onCallBack(data) {
        if (typeof data.notifications != "undefined") {
            for (var i = 0; i < data.notifications.length; i++) {
                pushNotification(data.notifications[i]);
            }
            ui.log_table.container.parent().scrollTop(ui.log_table.container.parent()[0].scrollHeight);
        }
    }

    function onFail(msg) {
        Alert.error(msg);
    }

    Polling.start(onCallBack, onFail);
};

//Holds necessary functions to manage (e.g. add, delete, edit...) the rules
RuleManager = {};
/**
 * Adds a new rule given by its name, trigger, action and state. If problems occur, a error alert will be shown.
 * @param name The name of the new rule
 * @param trigger The trigger class name of the new rule
 * @param action The action method name of the new rule
 * @param enabled True, when the new rule shall be enabled; false otherwise
 */
RuleManager.add = function (name, trigger, action, enabled) {
    //Perform a server request to add the rule
    Request.addRule(name, trigger, action, enabled, function (data) {
        //Success, show alert and update the rule table
        Alert.success('The rule "' + name + '" was added successfully.');
        RuleManager.refresh();
        //In case of an error show an error alert
    }, Alert.error);
};
/**
 * Deletes a existing rule given by its name. If problems occur, a error alert will be shown.
 * @param name The name of the rule to delete
 */
RuleManager.deleteRule = function (name) {
    //Perform a server request to delete the rule
    Request.deleteRule(name, function (data) {
        //Success, show alert and update the rule table
        Alert.success('The rule "' + name + '" was deleted successfully.');
        RuleManager.refresh();
        //In case of an error show an error alert
    }, Alert.error);
};
/**
 * Switches (toggles) the state of a rule given by its name. That means, if the rule is enabled, it will be disabled
 * after this call, otherwise it will be enabled. If problems occur, a error alert will be shown.
 * @param name The name of the rule which shall be toggled
 */
RuleManager.switch = function (name) {
    //Perform a server request to switch the rule's state
    Request.switchRule(name, function () {
        //Success, update the rule table
        RuleManager.refresh();
    }, function (msg) {
        //In case of an error show an error alert and refresh the rule table
        Alert.error(msg);
        RuleManager.refresh();
    });
};
/**
 * Refreshes (updates) the rule table with all rules that are stored in the server's database. If problems occur, a
 * error alert will be shown.
 */
RuleManager.refresh = function () {
    //Perform a server request to fetch all rules
    Request.getRules(function (data) {
        //Success, update the rule table now

        //Clear the table first
        ui.rule_table.body.empty();

        //Are there no rules?
        if (data.rules.length < 1) {
            //Display a message over all columns explaining that no rules could be found
            var row = $('<tr></tr>');
            row.append($('<td colspan="6">There are no rules to display.</td>'));
            //Append the message to the table
            ui.rule_table.body.append(row);
            return;
        }

        //Iterate over all received rules and append them to the database
        $.each(data.rules, function (index, rule) {
            //Create new table row
            var row = $('<tr></tr>');
            //Append name
            row.append($('<td><a href="#" class="rule_edit_name" data-pk="' + rule.name + '">' + rule.name +
                '</a></td>'));
            //Append trigger class name
            row.append($('<td><a href="#" class="rule_edit_trigger" data-pk="' + rule.name + '">' + rule.trigger +
                '</td>'));
            //Append action method name
            row.append($('<td><a href="#" class="rule_edit_action" data-pk="' + rule.name + '">' + rule.action +
                '</td>'));
            //Append creation timestamp
            row.append($('<td>' + Basics.formatDate(rule.timestamp) + '</td>'));

            //Create state switch
            var switch_container = $('<div class="material-switch pull-right"></div>');
            var switch_text = $('<span class="state">Inactive</span>');
            var switch_input = $('<input id="state_switch' + index + '" type="checkbox"/>');

            //Adjust the switch and its label to the current rule state
            if (rule.enabled) {
                switch_input.prop('checked', true);
                switch_text.text('Active');
                switch_text.addClass('enabled')
            }

            //Click action of the switch
            switch_input.change(function () {
                //Switch rule with a delay of one second (to perform the animation before)
                window.setTimeout(function () {
                    RuleManager.switch(rule.name);
                }, 1000);
            });
            //Append the switch and its label to the container
            switch_container.append(switch_input);
            switch_container.append($('<label for="state_switch' + index + '" class="label-success"></label>'));

            //Append the switch to the current table row
            row.append($('<td></td>').append($('<div class="switch"></div>').append(switch_container)));
            row.append($('<td></td>').append(switch_text));

            //Create a play button with which the rule can be triggered manually
            var trigger_button = $('<span class="trigger_rule glyphicon glyphicon-play-circle" data-toggle="tooltip"' +
                ' title="Triggers this rule and executes the action method"></span>');
            //Trigger action
            trigger_button.click(function () {
                //Trigger the rule
                RuleManager.triggerRule(rule.name);
            });
            trigger_button.tooltip();
            //Append the trigger button to the table
            row.append($('<td></td>').append(trigger_button));

            //Create a delete button with which the rule can be deleted
            var delete_button = $('<span class="delete_rule glyphicon glyphicon-trash" data-toggle="tooltip" ' +
                'title="Deletes this rule"></span>');
            //Delete action
            delete_button.click(function () {
                //Let the user confirm the deletion
                Confirm.show('<span class="glyphicon glyphicon-trash"></span>&nbsp;Delete rule', 'Do you really ' +
                    'want to delete the rule named "' + rule.name + '"?', function () {
                    //Delete the rule
                    RuleManager.deleteRule(rule.name);
                }, null, 'danger', 'primary');
            });
            delete_button.tooltip();
            //Append the deletion button to the table
            row.append($('<td></td>').append(delete_button));

            ui.rule_table.body.append(row);
        });
        //Make all rule fields editable
        ui.rule_table.makeEditable();
        //In case of an error show an error alert
    }, Alert.error)
};

/**
 * Triggers a existing rule given by its name. If problems occur, a error alert will be shown.
 * @param name The name of the rule to delete
 */
RuleManager.triggerRule = function (name) {
    //Perform a server request to trigger the rule
    Request.triggerRule(name, function (data) {
        //Success, show alert and update the rule table
        Alert.success('The rule "' + name + '" was triggered successfully.');
        //In case of an error show an error alert
    }, Alert.error);
};

//Holds functions that perform server ajax requests for the communication with the server
Request = {};
/**
 * Sends a server requests that lets the server add a new rule given by its fields. In case of success a callback
 * function will be called, otherwise a failure callback function will be executed.
 * @param name The name of the rule to add
 * @param trigger The trigger class name of the rule to add
 * @param action The action method name of the rule to add
 * @param enabled The state of the rule to add; true when the rule shall be enabled, false otherwise
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.addRule = function (name, trigger, action, enabled, callback, fail) {
    //Perform ajax request
    $.post("/add", {'name': name, 'trigger': trigger, 'action': action, 'enabled': enabled}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail('A unknown connection error occurred while trying to add the rule.')
    });
};

/**
 * Sends a server requests that lets the server delete a rule given by its name. In case of success a callback
 * function will be called, otherwise a failure callback function will be executed.
 * @param ruleName The name of the rule to delete
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.deleteRule = function (ruleName, callback, fail) {
    //Perform ajax request
    $.post("/delete", {'name': ruleName}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail('A unknown connection error occurred while trying to remove the rule.');
    });
};

/**
 * Sends a server requests that lets the server toggle a rule's state given by its name. In case of success a callback
 * function will be called, otherwise a failure callback function will be executed.
 * @param ruleName The name of the rule to toggle
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.switchRule = function (ruleName, callback, fail) {
    //Perform ajax request
    $.post("/switch", {'name': ruleName}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail("A unknown connection error occured while trying to switch the rule state.");
    });
};

/**
 * Sends a server requests that lets the server retrieve a list of all rules. In case of success a callback
 * function will be called, otherwise a failure callback function will be executed.
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.getRules = function (callback, fail) {
    //Perform ajax request
    $.post("/get", {}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail('A unknown connection error occurred while trying to load the rules.');
    });
};

/**
 * Sends a server requests that lets the server restart itself.
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.restart = function (callback, fail) {
    //Perform ajax request
    $.post("/restart", {}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail("A unknown connection error occured while trying to restart.");
    });
};

/**
 * Sends a server requests that lets the server shutdown itself.
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.shutdown = function (callback, fail) {
    //Perform ajax request
    $.post("/shutdown", {}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail("A unknown connection error occured while trying to shutdown.");
    });
};

/**
 * Sends a server requests that lets the server trigger a rule given by its name. In case of success a callback
 * function will be called, otherwise a failure callback function will be executed.
 * @param ruleName The name of the rule to trigger
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Request.triggerRule = function (ruleName, callback, fail) {
    //Perform ajax request
    $.post("/trigger", {'name': ruleName}, function (data) {
        //Server error?
        if (data.error) {
            //Error alert
            fail(data.msg);
            return;
        }

        //Success, call the callback function with parameters
        callback(data);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail('A unknown connection error occurred while trying to trigger the rule.');
    });
};

/* Holds functions that perform server ajax requests using the "long polling" technique in order to receive
 notifications from server live during runtime ("live log")*/
Polling = {};
//Turns the polling sequence on (true) or off (false)
Polling.run = false;
//Remembers the highest ID of the newest received notification
Polling.lastID = -1;
/**
 * Performs a polling server request in order to receive new notifications from the server during runtime. Uses long
 * polling, a request for fetching new notifications will be answered by the server when there are new notifications;
 * otherwise the request will be kept open and time out after plenty seconds, in the case, that there was no new
 * notification in this time. After the last request has ended a new request gets started. In case of success a
 * callback function will be called, otherwise a failure callback function will be executed.
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Polling.poll = function (callback, fail) {
    /**
     * Restarts or continues the polling sequence after a certain delay time; for instance, when the previous polling
     * request failed due to connection problems or polling was successful and may be continued.
     * @param delay The delay time in milliseconds
     */
    function restart(delay) {
        //Restart only when polling is turned on
        if (Polling.run) {
            //Restart polling after delay time
            window.setTimeout(function () {
                Polling.poll(callback, fail);
            }, delay);
        }
    }

    //Perform poll request using
    $.post("/poll", {'last_id': Polling.lastID}, function (data) {
        //Server error?
        if (data.error) {
            //Yes, show error alert and restart polling as far as it is still turned on
            fail(data.msg);
            restart(5000);
            return;
        }

        //Success, call the callback function with received reply data
        callback(data);

        //Check whether reply contains notifications
        if (typeof data.notifications != "undefined") {
            //Yes, so get ID of the newest message and save it as lastID for further polling requests
            Polling.lastID = data.notifications[data.notifications.length - 1].id;
        }

        //Continue polling sequence, init next polling request
        restart(1000);

    }, "json").fail(function () {
        //Connection error, call failure callback function
        fail("A unknown connection error occurred.");
        //Restart polling as far as it is still turned on
        restart(5000);
    });
};
/**
 * Initializes and starts the polling sequence. In case of success a allback function will be called, otherwise a
 * failure callback function will be executed.
 * @param callback The success callback function
 * @param fail The failure callback function
 */
Polling.start = function (callback, fail) {
    //Turn polling switch on
    Polling.run = true;
    //Start polling
    Polling.poll(callback, fail);
};

/**
 * Stops the long polling sequence. The newest (currently running) polling request will not be terminated, but no
 * further request will be started.
 */
Polling.stop = function () {
    //Turn polling switch off
    Polling.run = false;
};

//Holds basic functions for general purposes
Basics = {};
/**
 * Converts a given unix timestamp into a formatted string date: dd.mm.yyyy hh:mm:ss
 * @param time The unix timestamp to convert
 * @returns The formatted date string corresponding to the given timestamp
 */
Basics.formatDate = function (time) {
    /**
     * Internal function that converts a number to a number string that always consists of at least two digits.
     * If the given number has only one digit, a leading zero will be prepended. If the given number has more than two
     * digits, the returned string will also have that number of digits.
     * @param number The number to convert
     * @returns The two-digit string representing the given number
     */
    function twoDigit(number) {
        //Check the number of digits - less than two?
        if (number < 10) {
            //Prepend leading zero
            return "0" + number;
        }
        return number;
    }

    //Create date object from unix timestamp
    var date = new Date(time);

    //Retrieve date fields and return formatted date
    return twoDigit(date.getDate()) + "." + twoDigit(date.getMonth() + 1) + "." + date.getFullYear() + " " +
        twoDigit(date.getHours()) + ":" + twoDigit(date.getMinutes()) + ":" + twoDigit(date.getSeconds());
};
/**
 * Main function that is called when the page has been loaded. In it general settings are adjusted and initializing
 * functions are called.
 */
$(document).ready(function () {
    // Disable caching of AJAX responses
    $.ajaxSetup({
        cache: false
    });
    //Initialize the user interface with javascript
    ui.init();

    //Load rules and add them to the rule table for the first time
    RuleManager.refresh();
});
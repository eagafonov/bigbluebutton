(function(window){
    var handlers = {};

    window.bbbDeskshareViewer = {
        getHandler: function(id) {
            return handlers[id];
        },

        createHandler: function(id) {
            var handler = {
                    id:id,

                    // Callbacks
                    onCreated: null,
                    onViewStop: null,
                    onViewStart: null,

                    // methods
                    remove: function() {
                        handlers[id] = undefined;
                    }
                };

            handlers[id] = handler;

            return handler;
        },

        callHandler: function(id, method, args) {
            try {

                if (typeof(handlers[id]) === 'object') {
                    if (typeof(handlers[id][method]) === 'function') {
//                         console.log('[DESKSHARE] Call', id, method, args);
                        handlers[id][method].call(args);
                    } else {
//                         console.log('[DESKSHARE] Handler has not method', method);
                    }
                } else {
//                     console.log('[DESKSHARE] No handler for ID', id);
                }
            } catch (e) {
                if (console != undefined) {
                    console.log("Failed to perform swf2js call: ", e);
                }
            }
        }
    };

    window.bbbDeskshare_CallHelper = window.bbbDeskshareViewer.callHandler;
})(this)
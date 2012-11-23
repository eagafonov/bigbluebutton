(function(window){
    var handlers = {};

    window.bbbDeskshare = {
        addHandler: function(id, handler) {
            handlers[id] = handler;
        },

        getHandler: function(id) {
            return handlers[id];
        },
 
        createApplet: function(containerId, host, port, room, jarPath) {
            var container;
            var id;

            if (typeof(containerId) == 'string') {
                container = $("#" + containerId);
            } else {
                container = containerId;
                containerId = containerId.attr('id');
            }

            if (jarPath === undefined) {
                jarPath = 'bbb-deskshare-applet-0.8.jar'
            }

            var id = containerId + "_applet"+Math.floor(Math.random() * 100000000);

            var handler = {
                    id:id,
 
                    // Callbacks
                    onAppletInit: null,
                    onAppletStart:  null,
                    onAppletStop:  null,
                    onShareStart: null,

                    // methods
                    remove: function() {
                        window.jQuery("#"+this.id).parent().empty();
                    }
                };

            this.addHandler(id, handler);

            var applet = $("<applet>").attr('code', 'org.bigbluebutton.deskshare.client.DeskShareApplet.class')
                                        .attr('archive', jarPath+"?VTAG=00000000")
                                        .attr('id', id)
                                        .addClass('applet');

            $("<param/>").attr('name', 'ID').attr('value', id).appendTo(applet);
            $("<param/>").attr('name', 'ROOM').attr('value', room).appendTo(applet);
            $("<param/>").attr('name', 'IP').attr('value', host).appendTo(applet);
            $("<param/>").attr('name', 'PORT').attr('value', port).appendTo(applet);
            $("<param/>").attr('name', 'FULLSCREEN').attr('value', 'fullscreen').appendTo(applet);


            container.append(applet);

            return handler;
        }
    };
})(this)
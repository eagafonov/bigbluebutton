(function(window){
    window.bbbDeskshare = {
        handlers: {}, // TODO Hide it

        addHandler: function(id, handler) {
            this.handlers[id] = handler;
        },

        createApplet: function(containerId, host, port, room) {
            id = containerId + "_applet"+Math.floor(Math.random() * 100000000);

            var handler = {
                    id:id,
 
                    // Callbacks
                    onAppletCreate: null,
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
                                        .attr('archive', 'bbb-deskshare-applet-0.8.jar')
                                        .attr('id', id)
                                        .addClass('applet');

            $("<param/>").attr('name', 'ID').attr('value', id).appendTo(applet);
            $("<param/>").attr('name', 'ROOM').attr('value', room).appendTo(applet);
            $("<param/>").attr('name', 'IP').attr('value', host).appendTo(applet);
            $("<param/>").attr('name', 'PORT').attr('value', port).appendTo(applet);
            $("<param/>").attr('name', 'FULLSCREEN').attr('value', 'fullscreen').appendTo(applet);


            $("#" + containerId).append(applet);

            return handler;
        }
    };
})(this)
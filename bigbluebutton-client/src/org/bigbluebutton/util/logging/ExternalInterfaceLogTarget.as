package org.bigbluebutton.util.logging
{
    import mx.core.mx_internal;
    import mx.logging.targets.LineFormattedTarget;
    import flash.external.ExternalInterface;

    use namespace mx_internal;

    public class ExternalInterfaceLogTarget extends LineFormattedTarget
    {
        public function ExternalInterfaceLogTarget()
        {
            super();
        }

        override mx_internal function internalLog(message:String):void {
            ExternalInterface.call("flash_trace", message);
        }
    }
}
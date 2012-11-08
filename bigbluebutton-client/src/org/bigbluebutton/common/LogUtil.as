/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
*
* Copyright (c) 2010 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 2.1 of the License, or (at your option) any later
* version.
*
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
* 
*/
package org.bigbluebutton.common
{
	import mx.logging.ILogger;
	import mx.logging.Log;
	
	public class LogUtil
	{
		public static const LOGGER:String = "BBBLOGGER";
		
		public static function debug(... args):void
		{
            var message:String = "";

            for (var i:uint = 0; i < args.length; i++)
            {
                message += String(args[i])+" ";
            }

			logger.debug(message);
		}

		public static function info(... args):void
		{
            var message:String = "";

            for (var i:uint = 0; i < args.length; i++)
            {
                message += String(args[i])+" ";
            }
			logger.info(message);
		}
		
		public static function error(... args):void
		{
            var message:String = "";

            for (var i:uint = 0; i < args.length; i++)
            {
                message += String(args[i])+" ";
            }
			logger.error(message);
		}

		public static function fatal(... args):void
		{
            var message:String = "";

            for (var i:uint = 0; i < args.length; i++)
            {
                message += String(args[i])+" ";
            }

			logger.fatal(message);
		}
		
		public static function warn(... args):void
		{
            var message:String = "";

            for (var i:uint = 0; i < args.length; i++)
            {
                message += String(args[i])+" ";
            }

			logger.warn(message);
		}
		
		private static function get logger():ILogger {
			return Log.getLogger(LOGGER);
		}
	}
}
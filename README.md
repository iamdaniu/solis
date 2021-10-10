# solis
Data logger and multiplexer for binary data received via UDP.
I wrote this to be able to analyse Solis inverter data sent by the data logging stick, hence the name.

It relies on environment variable values to define its behavior.

Available environment variables:
- general:
 - listen_port - the udp port to listen to (required)
- console:
 - console_disabled - set to disable output
 - console_line_length - bytes per output line (default 8)
- file:
 - file_storage_dir - target directory (required)
- udp resend:
 - udp_target_host - target host (required)
 - udp_target_port - target port (default 9999)
 - udp_send_port - local port to send from (default 9999)

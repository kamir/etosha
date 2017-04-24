import org.etosha.cmd;
val log = new org.etosha.cmd.EtoshaContextLogger;

val cmd1 = new Array[String](2)
cmd1(0)="ECLShell"
cmd1(1)="list"
val status1 = org.etosha.cmd.EtoshaContextLogger.main( cmd1 );

val cmd2 = new Array[String](2)
cmd2(0)="ECLShell"
cmd2(1)="note"
val status2 = org.etosha.cmd.EtoshaContextLogger.main( cmd2 );

val cmd3 = new Array[String](2)
cmd3(0)="ECLShell"
cmd3(1)="cfg"
val status3 = org.etosha.cmd.EtoshaContextLogger.main( cmd3 );


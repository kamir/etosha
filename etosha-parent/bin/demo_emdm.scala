import org.etosha.mi.ModelInspector
import org.etosha.tools.profiler.EtoshaModelHandler

var o = new org.etosha.mi.ModelInspector
o.run( new EtoshaModelHandler )

EtoshaModelHandler.init( "/GITHUB/ETOSHA.WS/etosha/etosha-parent/data/in/modelrepo" )
o.run


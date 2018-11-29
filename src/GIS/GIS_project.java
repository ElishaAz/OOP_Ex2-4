package GIS;

import java.util.Set;

public interface GIS_project<P extends GIS_layer> extends Set<P>{
	public Meta_data get_Meta_data();
	
}

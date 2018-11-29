package GIS;

import java.util.Set;

public interface GIS_layer<E extends GIS_element> extends Set<E>
{
	public Meta_data get_Meta_data();

}

/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import java.util.Locale;

import il.ac.technion.cs.smarthouse.applications.dashboard.model.InfoCollector;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.services.file_system_service.FileSystemService;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class ClockWidget extends BasicWidget {

	public ClockWidget(WidgetType t, Double tileSize, InfoCollector data) {
		super(t,tileSize, data);
		builder.locale(Locale.US).dateVisible(true).running(true);
	}

	@Override
	public String getTitle() {
		return "Clock Widget";
	}

	@Override
	public void updateAutomaticallyFrom(FileSystemService fs) {
		return;
	}

}

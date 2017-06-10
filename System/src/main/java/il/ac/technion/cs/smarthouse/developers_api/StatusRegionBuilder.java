package il.ac.technion.cs.smarthouse.developers_api;

import java.util.Optional;
import java.util.function.Function;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * GUI layout - status region
 * <p>
 * A region for read-only fields
 * @author RON
 * @since 10-06-2017
 */
public final class StatusRegionBuilder extends AbstractRegionBuilder {
    public StatusRegionBuilder() {
        super.setTitle("Status");
    }

    @Override
    public StatusRegionBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    public <T> StatusRegionBuilder addStatusField(String title, DataObject<T> bindingDataObject) {
        return addStatusField(title, bindingDataObject, null);
    }

    public <T> StatusRegionBuilder addStatusField(String title, DataObject<T> bindingDataObject,
                    Function<Optional<T>, Color> colorFunction) {
        final Label l = createStatusLabel(bindingDataObject.getDataStr());
        
        bindingDataObject.addOnDataChangedListener(d -> {
            l.setText(d.get() + "");
            if (colorFunction != null)
                l.setTextFill(colorFunction.apply(d));
        });
        addAppBuilderItem(new AppBuilderItem(title, l));
        return this;
    }
    
    public StatusRegionBuilder addTimerStatusField(String title, DataObject<Boolean> timerToggle, DataObject<Double> timerDuration) {
        final Label timeLabel = createStatusLabel("");
        final Timeline timeline;
        final DoubleProperty timeSeconds = new SimpleDoubleProperty();
        final DataObject<Duration> time = new DataObject<>(Duration.ZERO);
        
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time.setData(time.getData().add(((KeyFrame) ¢.getSource()).getTime()));
            timeSeconds.set(time.getData().toSeconds());
            timeLabel.setText(timeSeconds.get() + " [sec]");
            timerDuration.setData(timeSeconds.get());
        }));
        
        timerToggle.addOnDataChangedListener(b->b.ifPresent(v->{
            if (v) {
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            } else {
                timeline.stop();
                time.setData(Duration.ZERO);
                timeSeconds.set(time.getData().toSeconds());
            }
        }));
        
        addAppBuilderItem(new AppBuilderItem(title, timeLabel));
        return this;
    }
    
    private Label createStatusLabel(String s) {
        final Label l = new Label(s);
        l.setFont(Font.font(14));
        return l;
    }
}

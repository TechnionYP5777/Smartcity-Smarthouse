package il.ac.technion.cs.smarthouse.developers_api;

import java.util.Optional;
import java.util.function.Function;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
                    Function<T, Color> colorFunction) {
        final Label l = createStatusLabel(bindingDataObject.getDataAsString());
        
        bindingDataObject.addOnDataChangedListener(d -> {
            l.setText(d.getDataAsString());
            setColor(d, colorFunction, l);
        });
        
        setColor(bindingDataObject, colorFunction, l);
        
        addAppBuilderItem(new AppBuilderItem(title, l));
        return this;
    }
    
    public StatusRegionBuilder addTimerStatusField(String title, DataObject<Boolean> timerToggle, DataObject<Double> timerDuration) {
        return addTimerStatusField(title, timerToggle, timerDuration, null);
    }
    
    public StatusRegionBuilder addTimerStatusField(String title, DataObject<Boolean> timerToggle, DataObject<Double> timerDuration, Function<Double, Color> colorFunction) {
        final Label timeLabel = createStatusLabel("");
        final Timeline timeline;
        final DataObject<Duration> time = new DataObject<>(Duration.ZERO);
        
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time.setData(time.getData().add(((KeyFrame) ¢.getSource()).getTime()));
            timerDuration.setData(time.getData().toSeconds());
            timeLabel.setText(timerDuration.getData() + " [sec]");
        }));
        
        timerToggle.addOnDataChangedListener(v->{
            if (v.getData()) {
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            } else {
                timeline.stop();
                time.setData(Duration.ZERO);
                timerDuration.setData(time.getData().toSeconds());
                timeLabel.setText(timerDuration.getData() + " [sec]");
            }
        });
        
        if (colorFunction != null)
            timerDuration.addOnDataChangedListener(d->setColor(d, colorFunction, timeLabel));
        
        addAppBuilderItem(new AppBuilderItem(title, timeLabel));
        return this;
    }
    
    private Label createStatusLabel(String s) {
        final Label l = new Label(s);
        l.setFont(Font.font(14));
        return l;
    }
    
    private <T> void setColor(DataObject<T> bindingDataObject, Function<T, Color> colorFunction, Label l) {
        if (colorFunction != null && bindingDataObject != null && bindingDataObject.getData() != null)
            l.setTextFill(Optional.ofNullable(colorFunction.apply(bindingDataObject.getData())).orElse(Color.BLACK));
    }
}

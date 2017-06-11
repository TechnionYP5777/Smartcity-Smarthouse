package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.ColorRange;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.StatusRegionBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Implementation of {@link StatusRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class StatusRegionBuilderImpl extends AbstractRegionBuilder implements StatusRegionBuilder {
    public StatusRegionBuilderImpl() {
        super.setTitle("Status");
    }

    @Override
    public StatusRegionBuilderImpl setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public <T> StatusRegionBuilderImpl addStatusField(String title, GuiBinderObject<T> bindingDataObject) {
        final Label l = createStatusLabel(bindingDataObject.getDataAsString());

        bindingDataObject.addOnDataChangedListener(d -> l.setText(d.getDataAsString()));

        addAppBuilderItem(new AppBuilderItem(title, l));
        return this;
    }

    @Override
    public <T extends Comparable<T>> StatusRegionBuilderImpl addStatusField(String title, GuiBinderObject<T> bindingDataObject,
                    ColorRange<T> r) {        
        final Label l = createStatusLabel(bindingDataObject.getDataAsString());

        bindingDataObject.addOnDataChangedListener(d -> {
            l.setText(d.getDataAsString());
            setColor(d, r, l);
        });

        setColor(bindingDataObject, r, l);

        addAppBuilderItem(new AppBuilderItem(title, l));
        return this;
    }

    @Override
    public StatusRegionBuilderImpl addTimerStatusField(String title, GuiBinderObject<Boolean> timerToggle,
                    GuiBinderObject<Double> timerDuration) {
        return addTimerStatusField(title, timerToggle, timerDuration, null);
    }

    @Override
    public StatusRegionBuilderImpl addTimerStatusField(String title, GuiBinderObject<Boolean> timerToggle,
                    GuiBinderObject<Double> timerDuration, ColorRange<Double> d) {
        final Label timeLabel = createStatusLabel("");
        final Timeline timeline;
        final GuiBinderObject<Duration> time = new GuiBinderObject<>(Duration.ZERO);

        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time.setData(time.getData().add(((KeyFrame) ¢.getSource()).getTime()));
            timerDuration.setData(time.getData().toSeconds());
            timeLabel.setText(timerDuration.getData() + " [sec]");
        }));

        timerToggle.addOnDataChangedListener(v -> {
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

        if (d != null)
            timerDuration.addOnDataChangedListener(v -> setColor(v, d, timeLabel));

        addAppBuilderItem(new AppBuilderItem(title, timeLabel));
        return this;
    }

    private Label createStatusLabel(String s) {
        final Label l = new Label(s);
        l.setFont(Font.font(14));
        return l;
    }

    private <T extends Comparable<T>> void setColor(GuiBinderObject<T> bindingDataObject, ColorRange<T> r, Label l) {
        if (r != null && bindingDataObject != null && bindingDataObject.getData() != null)
            l.setTextFill(r.getColorOfValue(bindingDataObject.getData()));
    }
}

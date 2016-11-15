package com.xenoage.zong.io;

import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.layouter.Target;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

import static com.xenoage.zong.core.format.LayoutFormat.defaultLayoutFormat;

/**
 * Creates a {@link ScoreDoc} from a {@link Score}.
 *
 * @author Andreas Wenger
 */
public class ScoreDocFactory {

    /** If true, an error layout is used when there a problems during layouting the score.
     Otherwise, an exception is thrown.
     TODO: maybe provide this value over dependency injection */
    @Getter @Setter private static boolean isErrorLayoutEnabled = true;


    /**
     * Creates a {@link ScoreDoc} instance from the given score.
     * TIDY: move elsewhere, e.g. in a ScoreDocFactory class
     */
    public ScoreDoc read(Score score)
            throws InvalidFormatException, IOException {

        //page format
        LayoutFormat layoutFormat = defaultLayoutFormat;
        Object oLayoutFormat = score.getMetaData().get("layoutformat");
        if (oLayoutFormat instanceof LayoutFormat) {
            layoutFormat = (LayoutFormat) oLayoutFormat;
        }
        LayoutDefaults layoutDefaults = new LayoutDefaults(layoutFormat);

        //create the document
        ScoreDoc ret = new ScoreDoc(score, layoutDefaults);
        Layout layout = ret.getLayout();

        //layout basics
        PageFormat pageFormat = layoutFormat.getPageFormat(0);
        Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
        Point2f framePos = new Point2f(pageFormat.getMargins().getLeft() + frameSize.width / 2,
                pageFormat.getMargins().getTop() + frameSize.height / 2);

        //layout the score to find out the needed space
        Target target = Target.completeLayoutTarget(new ScoreLayoutArea(frameSize));
        ScoreLayouter layouter = new ScoreLayouter(ret, target);
        ScoreLayout scoreLayout = isErrorLayoutEnabled ?
                layouter.createScoreLayout() : layouter.createLayoutWithExceptions();

        //create and fill at least one page
        if (scoreLayout.frames.size() > 1) {
            //normal layout: one frame per page
            ScoreFrameChain chain = null;
            for (int i = 0; i < scoreLayout.frames.size(); i++) {
                Page page = new Page(pageFormat);
                layout.addPage(page);
                ScoreFrame frame = new ScoreFrame();
                frame.setPosition(framePos);
                frame.setSize(frameSize);
                //TEST frame = frame.withHFill(NoHorizontalSystemFillingStrategy.getInstance());
                page.addFrame(frame);
                if (chain == null) {
                    chain = new ScoreFrameChain(score);
                    chain.setScoreLayout(scoreLayout);
                }
                chain.add(frame);
            }
        }
        else {
            //no frames: create a single empty page
            Page page = new Page(pageFormat);
            layout.addPage(page);
        }

        return ret;
    }

}

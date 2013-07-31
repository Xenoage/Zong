package com.xenoage.zong.core.music.direction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * Class for a dynamics sign like forte, piano, sforzando and so on.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class Dynamics
	extends Direction {

	@Getter private final DynamicsType type;


}

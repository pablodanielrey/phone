package ar.com.dcsys.phone.assistance.client.phone;

import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.googlecode.mgwt.ui.client.widget.animation.Animation;
import com.googlecode.mgwt.ui.client.widget.animation.Animations;


public class PhoneAnimationMapper implements AnimationMapper {
	
	@Override
	public Animation getAnimation(Place oldPlace, Place newPlace) {
		return Animations.SLIDE;
	}


}

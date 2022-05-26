package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private final Attack attack;

    //BUILDER
    public AttackEvent(Attack newAttack){
        attack=newAttack;
    }
	//GETTER
    public Attack getAttack() {return  attack;}
  }

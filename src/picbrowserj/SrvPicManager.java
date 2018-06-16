/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

/**
 *
 * @author jkhome
 */
interface SrvPicManagerListener {
    void EventPics_added(); //Picture loaded for browsing
    void EventPics_moved(); 
    void EventPics_viewed(); //Picture selected for viewing
    void EventPics_new(); 
}
public class SrvPicManager 
        extends picbrowserj.Observable<SrvPicManagerListener> 
        implements ModelListener,java.util.Observer{
    
    private static SrvPicManager s_Instance;
    private SrvPicManager() {
        ModelPictures.init();
    }

    @Override
   public void update(Observable obs, Object obj)
   {
       try {
            MyObservable.UpdateReason reason = MyObservable.UpdateReason.class.cast(obj);
            switch(reason.Reason){
                case Pics_new:
                   // updatePicture(reason.Picture);
                    ModelPictures.getInstance().AddNewPicture(reason.Picture);
                    onEventPics_added(); 
                    break;
                default:
                    break;
            }
        } catch (ClassCastException e) {
        }
   }
    public static boolean init() {
        if (s_Instance==null) {
            s_Instance= new SrvPicManager();
            s_Instance.loadData();
            return true;
        } else {
            return false;
        }
    }
    public static boolean shutdown () {
        return true;
    }
    public static SrvPicManager getInstance() {
        if (s_Instance==null) {
            //Todo throw exception
        }
        return s_Instance;
    }
    public ArrayList<DatPicture> getPicturesToView() {
        return ModelPictures.getInstance().getAllPicture();
    }
   

    private void loadData(){
        onEventPics_added();
    }
    
    public void SavePicture(DatPicture Pic) {
        ModelPictures.getInstance().SavePicture(Pic);
    }
    
    public void DoCmd(picbrowserj.Interface.CmdInterface Cmd) {
        Cmd.Redo();
    }
    // Events raised by Service
    void onEventPics_added() {
        for(SrvPicManagerListener l: listeners) l.EventPics_added();
    }
    void onEventPics_moved(){
        for(SrvPicManagerListener l: listeners) l.EventPics_added();
    } 
    void onEventPics_viewed(){
        for(SrvPicManagerListener l: listeners) l.EventPics_added();
    }
    void onEventPics_new(){
        for(SrvPicManagerListener l: listeners) l.EventPics_added();
    }
    // Events received from Model
    @Override
    public void EventPics_added() {
        onEventPics_added();
    }

    @Override
    public void EventPics_moved() {
        onEventPics_moved();
    }

    @Override
    public void EventPics_viewed() {
        onEventPics_viewed();
    }

    @Override
    public void EventPics_new() {
        onEventPics_new();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.util.ArrayList;

/**
 *
 * @author jkhome
 */
interface SrvPicManagerListener {
    void EventPics_added(DatPicture Picture); //Picture loaded for browsing
    void EventPics_moved(DatPicture Picture); 
    void EventPics_viewed(DatPicture Picture); //Picture selected for viewing
    void EventPics_new(DatPicture Picture); 
}
public class SrvPicManager 
        extends picbrowserj.Observable<SrvPicManagerListener> 
        implements ModelListener//,java.util.Observer
{
    
    private static SrvPicManager s_Instance;
    private SrvPicManager() {
        ModelPictures.init();
    }

   /*@Override
   public void update(Observable obs, Object obj)
   {
       try {
            MyObservable.UpdateReason reason = MyObservable.UpdateReason.class.cast(obj);
            switch(reason.Reason){
                case Pics_new:
                   // updatePicture(reason.Picture);
                    ModelPictures.getInstance().AddNewPicture(reason.Picture);
                    onEventPics_added(null); 
                    break;
                default:
                    break;
            }
        } catch (ClassCastException e) {
        }
   }*/
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
        onEventPics_added(null);
    }
    
    public void SavePicture(DatPicture Pic) {
        ModelPictures.getInstance().SavePicture(Pic);
    }
    
    public void DoCmd(picbrowserj.Interface.CmdInterface Cmd) {
        Cmd.Redo();
    }
    // Events raised by Service
    void onEventPics_added(DatPicture Picture) {
        for(SrvPicManagerListener l: listeners) l.EventPics_added(Picture);
    }
    void onEventPics_moved(DatPicture Picture){
        for(SrvPicManagerListener l: listeners) l.EventPics_moved(Picture);
    } 
    void onEventPics_viewed(DatPicture Picture){
        for(SrvPicManagerListener l: listeners) l.EventPics_viewed(Picture);
    }
    void onEventPics_new(DatPicture Picture){
        for(SrvPicManagerListener l: listeners) l.EventPics_new(Picture);
    }
    // Events received from Model
    @Override
    public void EventPics_added(DatPicture Picture) {
        onEventPics_added(Picture);
    }

    @Override
    public void EventPics_moved(DatPicture Picture) {
        onEventPics_moved(Picture);
    }

    @Override
    public void EventPics_viewed(DatPicture Picture) {
        onEventPics_viewed(Picture);
    }

    @Override
    public void EventPics_new(DatPicture Picture) {
        onEventPics_new(Picture);
    }
}

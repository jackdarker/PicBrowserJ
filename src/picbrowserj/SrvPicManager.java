/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author jkhome
 */
public class SrvPicManager extends Observable implements Observer{
    private static ArrayList<InterfaceSrvObserver> s_Observers;
    //private static ArrayList<String> s_Pictures;
    
    private static SrvPicManager s_Instance;
    private SrvPicManager() {
        s_Observers = new ArrayList<InterfaceSrvObserver>();
        ModelPictures.init();
    }
    private void do_update () {
        
        notifyObservers();
        /*for(int i=0; i< s_Observers.size();i++) {
            InterfaceSrvObserver Obj = s_Observers.get(i);
            Obj.update(reason);
        }*/
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
   @Override
   public void update(Observable obs, Object obj)
   {
       try {
            MyObservable.UpdateReason reason = MyObservable.UpdateReason.class.cast(obj);
            switch(reason.Reason){
                case Pics_new:
                   // updatePicture(reason.Picture);
                    ModelPictures.getInstance().AddNewPicture(reason.Picture);
                    this.setChanged();
                    this.notifyObservers(reason);
                    break;
                default:
                    break;
            }
        } catch (ClassCastException e) {
        }
   }
    public void registerObserver (InterfaceSrvObserver listener) {
        if (listener==null) return;
        if (s_Observers.contains(listener)) return;
        s_Observers.add(listener);
    }
    public void unregisterObserver (InterfaceSrvObserver listener) {
        if (s_Observers.contains(listener)) {
            s_Observers.remove(listener);
        }
    }
    MyObservable MyObs = new MyObservable();
    private void loadData(){
        
        MyObservable.UpdateReason reason;
        reason = MyObs.new UpdateReason(MyObservable.updateReasonEnum.Pics_added,"");
        this.setChanged();
        this.notifyObservers(reason);
    }
    
    public void SavePicture(DatPicture Pic) {
        ModelPictures.getInstance().SavePicture(Pic);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author jkhome
 */
public class SrvPicManager extends Observable{
    private static ArrayList<InterfaceSrvObserver> s_Observers;
    //private static ArrayList<String> s_Pictures;
    
    private static SrvPicManager s_Instance;
    private SrvPicManager() {
        s_Observers = new ArrayList<>();
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
    private void loadData(){
        
        this.setChanged();
        this.notifyObservers(updateReason.Pics_added);
    }
    
    public enum updateReason {
        Pics_added, Pics_moved; 
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learn.zkclient.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 *
 * @author Chathuranga
 */
public class App {
    
    public static void main(String args[]) throws KeeperException, InterruptedException{
        //https://www.tutorialspoint.com/zookeeper/zookeeper_quick_guide.htm
        System.out.println("Simple read/write test...");
        new SimpleReadWrite().write("/test/write");        
        new SimpleReadWrite().read("/test/write");
        //new App();
                       
    }

    public App() throws KeeperException, InterruptedException {
        ZooKeeper zk        ;
        try {
            zk = new ZooKeeper("localhost:40001", 3000, (WatchedEvent we) -> {
                if (we.getState() == KeeperState.SyncConnected) {
                    synchronized(this){
                        System.out.println("Notified");
                        this.notify();
                    }
                    
                 }else{
                    System.out.println(we.getState().toString());
                    notify();
                }                
            });

            
        } catch (Exception ex) {
            notify();
            return;
        }            
            
            synchronized(this){
                System.out.println("Waiting...");
                wait(3000);
            }
            
            Stat s = zk.exists("/test", ((we) -> {
                System.out.println("Created");
            }));
            
            System.out.println("Creating...");
            zk.create("/test", "MytestData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //zk.delete("/test", 0);
            
            zk.close();
        
    }
    
    
    

}

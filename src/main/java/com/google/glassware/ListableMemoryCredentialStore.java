
package com.google.glassware;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListableMemoryCredentialStore implements CredentialStore {

  private final Lock lock = new ReentrantLock();

  private Database db = new Database();

  public void store(String userId, Credential credential) {
    lock.lock();
    try {
        db.insertUser(credential.getAccessToken(),
                credential.getRefreshToken(),
                credential.getExpirationTimeMilliseconds(),
                userId);
    } finally {
      lock.unlock();
    }
  }

  public void delete(String userId, Credential credential) {
    lock.lock();
    try {
        db.deleteNewUser(userId);
    } finally {
      lock.unlock();
    }
  }

  public boolean load(String userId, Credential credential) {
    lock.lock();
    try {
        String [] result = db.readUserCredentials(userId);

        if(result.length > 0){
            credential.setAccessToken(result[0]);
            credential.setRefreshToken(result[1]);
            credential.setExpirationTimeMilliseconds(Long.parseLong(result[2]));
            return true;
        }
    } finally {
      lock.unlock();
    }
      return false;
  }
}
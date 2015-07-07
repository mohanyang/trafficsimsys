# Introduction #

simulator kernel enumerates all road and all vehicles on it, but when vehicle make turns, it would be deleted from
them, causing the iterator to throw a concurrent modification error.


# Details #

use lazy removal to avoid this, add each removed element into a new list without actually deleting them.
after iteration finished, user **has to** call performRemoval explicitly.
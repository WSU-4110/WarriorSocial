# Bug Repository:

(4/10/2021 : Active)

**BUG**: AlexChen2 branch bug when swapping from API level 26 to API level 30.

**ERROR**: java.lang.SecurityException: Permission Denial: opening provider com.android.providers.media.MediaDocumentsProvider from ProcessRecord{1991732 5945:com.example.warriorsocial/u0a154} (pid=5945, uid=10154) requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs

**Reasoning**: Likely something to do with depreciation of old functions or some newer permission scheme introduced in later API's

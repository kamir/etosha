package data;

/**
 *
 * 
 * 
 * @author kamir
 */
class ProjecktCFG {

    boolean useGoogleDocs = false;
    boolean useHadoop = false;
    boolean useWebSceye = true;
    boolean useCFG = false;

    public boolean isUseCFG() {
        return useCFG;
    }

    public void setUseCFG(boolean useCFG) {
        this.useCFG = useCFG;
    }

    public boolean isUseGoogleDocs() {
        return useGoogleDocs;
    }

    public void setUseGoogleDocs(boolean useGoogleDocs) {
        this.useGoogleDocs = useGoogleDocs;
    }

    public boolean isUseHadoop() {
        return useHadoop;
    }

    public void setUseHadoop(boolean useHadoop) {
        this.useHadoop = useHadoop;
    }

    public boolean isUseWebSceye() {
        return useWebSceye;
    }

    public void setUseWebSceye(boolean useWebSceye) {
        this.useWebSceye = useWebSceye;
    }

}

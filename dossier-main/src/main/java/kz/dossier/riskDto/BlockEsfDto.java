package kz.dossier.riskDto;

public class BlockEsfDto {
    private String name;
    private String dateOfBlock;
    private String blockReason;
    private String dateOfRevert;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBlock() {
        return dateOfBlock;
    }

    public void setDateOfBlock(String dateOfBlock) {
        this.dateOfBlock = dateOfBlock;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public String getDateOfRevert() {
        return dateOfRevert;
    }

    public void setDateOfRevert(String dateOfRevert) {
        this.dateOfRevert = dateOfRevert;
    }
}

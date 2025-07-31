const hre = require("hardhat");

async function main() {
  const NewsPublisher = await hre.ethers.getContractFactory("NewsPublisher");
  const newsPublisher = await NewsPublisher.deploy();
  await newsPublisher.waitForDeployment();
  console.log(`NewsPublisher deployed to ${await newsPublisher.getAddress()}`);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});

const { expect } = require("chai");

describe("NewsPublisher", function () {
  let NewsPublisher, newsPublisher, owner, other;

  beforeEach(async function () {
    [owner, other] = await ethers.getSigners();
    const NewsPublisherFactory = await ethers.getContractFactory("NewsPublisher");
    newsPublisher = await NewsPublisherFactory.deploy();
    await newsPublisher.waitForDeployment();
  });

  it("owner can publish articles", async function () {
    await newsPublisher.publishArticle("Hello", "Content");
    const articles = await newsPublisher.getAllArticles();
    expect(articles.length).to.equal(1);
    expect(articles[0].title).to.equal("Hello");
  });

  it("non-owner cannot publish", async function () {
    await expect(
      newsPublisher.connect(other).publishArticle("Hi", "Body")
    ).to.be.revertedWith("Not owner");
  });

  it("users can vote", async function () {
    await newsPublisher.publishArticle("A", "B");
    await newsPublisher.connect(other).vote(0);
    const articles = await newsPublisher.getAllArticles();
    expect(articles[0].voteCount).to.equal(1);
  });

  it("prevent double voting", async function () {
    await newsPublisher.publishArticle("A", "B");
    await newsPublisher.connect(other).vote(0);
    await expect(newsPublisher.connect(other).vote(0)).to.be.revertedWith(
      "already voted"
    );
  });
});

// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/// @title News Publisher contract
/// @notice Allows the owner to publish articles and anyone to vote on them
contract NewsPublisher {
    struct Article {
        uint256 id;
        string title;
        string content;
        address author;
        uint256 voteCount;
    }

    Article[] private articles;
    mapping(uint256 => mapping(address => bool)) private voted;
    address public owner;
    uint256 private nextId;

    event ArticlePublished(uint256 indexed id, address indexed author, string title);
    event Voted(uint256 indexed id, address indexed voter, uint256 votes);

    modifier onlyOwner() {
        require(msg.sender == owner, "Not owner");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    function transferOwnership(address newOwner) external onlyOwner {
        require(newOwner != address(0), "zero address");
        owner = newOwner;
    }

    function publishArticle(string calldata title, string calldata content) external onlyOwner returns (uint256) {
        articles.push(Article({
            id: nextId,
            title: title,
            content: content,
            author: msg.sender,
            voteCount: 0
        }));
        emit ArticlePublished(nextId, msg.sender, title);
        nextId++;
        return nextId - 1;
    }

    function vote(uint256 id) external {
        require(id < articles.length, "invalid id");
        require(!voted[id][msg.sender], "already voted");
        voted[id][msg.sender] = true;
        articles[id].voteCount++;
        emit Voted(id, msg.sender, articles[id].voteCount);
    }

    function getAllArticles() external view returns (Article[] memory) {
        return articles;
    }
}
